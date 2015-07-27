package io.ttl;

import io.ttl.val.*;

public class Parser {

    private Env env;

    private Lex lex;

    public Parser(Env env, Lex lex) {
        this.env = env;
        this.lex = lex;
    }

    // expr ::= factorlevel moreterms
    // moreterms ::= + factorlevel moreterms
    //               | - factorlevel moreterms
    //               | <E>
    // factorlevel ::= atom morefactors
    // morefactors ::= * atom morefactors
    //                 | / atom morefactors
    //                 | % atom morefactors
    //                 | <E>
    // atom ::= <number> | <string> | <id> | (expr)
    public String parse() {
        return "" + expr();
    }

    private Val expr() {
        return moreterms(factorlevel());
    }

    private Val moreterms(Val lval) {
        Token t = lex.nextToken();
        if (t.matchOperator("+")) {
            Val rval = factorlevel();
            Val eval = null;
            if (lval.getType() == Val.Type.string) {
                eval = new Str(lval.evalStr(env) + rval.evalStr(env));
            } else {
                eval = new Num(lval.evalNum(env) + rval.evalNum(env));
            }
            return moreterms(eval);
        } else if (t.matchOperator("-")) {
            Val rval = factorlevel();
            Val eval = new Num(lval.evalNum(env) - rval.evalNum(env));
            return moreterms(eval);
        } else {
            lex.returnToken();
            return lval;
        }
    }

    private Val factorlevel() {
        return morefactors(atom());
    }

    private Val morefactors(Val lval) {
        Token t = lex.nextToken();
        if (t.matchOperator("*")) {
            Val rval = atom();
            Val eval = new Num(lval.evalNum(env) * rval.evalNum(env));
            return morefactors(eval);
        } else if (t.matchOperator("/")) {
            Val rval = atom();
            Val eval = new Num(lval.evalNum(env) / rval.evalNum(env));
            return morefactors(eval);
        } else if (t.matchOperator("%")) {
            Val rval = atom();
            Val eval = new Num(lval.evalNum(env) % rval.evalNum(env));
            return morefactors(eval);
        } else {
            lex.returnToken();
            return lval;
        }
    }

    private Val atom() {
        Token t = lex.nextToken();
        if (t.type == Token.Type.number) {
            return new Num((Double)t.value);
        } else if (t.type == Token.Type.string) {
            return new Str("" + t.value);
        } else if (t.type == Token.Type.id) {
            return new Id("" + t.value);
        } else if (t.matchOperator("(")) {
            Val v = expr();
            t = lex.nextToken();
            if (!t.matchOperator(")")) {
                throw new EvalException("lexical error: ) was expected");
            }
            return v;
        } else {
            lex.returnToken();
            return Nil.NIL;
        }
    }

}
