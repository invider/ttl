package io.ttl;

import io.ttl.val.*;

public class Parser {

    private Lex lex;

    public Parser(Lex lex) {
       this.lex = lex;
    }

    // BASIC SYNTAX
    // expr ::= expr + term | expr - term | term
    // term ::= term * factor | term / factor | term % factor | factor
    // factor ::= <number> | <string> | <id> | (expr)

    // BASIC SYNTAX WITH ELIMINATED LEFT RECURSION
    // expr ::= term moreterms
    // moreterms ::= + term moreterms | - term moreterms | <E>
    // term ::= factor morefactors
    // morefactors ::= * factor morefactors | / factor morefactors | % factor morefactors | <E>
    // factor ::= <number> | <string> | <id> | (expr)

    // SYNTAX
    // flow ::= expr moreflow
    // moreflow  ::= ,flow | <E>
    // expr ::= pairlevel morepairs
    // pairlevel ::= condlevel morepairs
    // morepairs ::= :: condlevel morepairs | <E>
    // condlevel ::= levelor morecond
    // morecond ::=
    //          | ? expr
    //          | ? expr !! expr
    //          | ?~ expr
    //          | # expr
    //          | <E>
    // levelor ::= leveland moreor
    // moreor ::= || leveland moreor | <E>
    // leveland :: levelcomp moreand
    // moreand ::= && levelcomp moreand | <E>
    // levelcomp ::= levelterm morecomp
    // morecomp ::= > levelterm morecomp
    //                | < levelterm morecomp
    //                | >= levelterm morecomp
    //                | <= levelterm morecomp
    //                | = levelterm morecomp
    //                | <> levelterm morecomp
    //                | <E>
    // levelterm ::= levelfactor moreterms
    // moreterms ::= + levelfactor moreterms | - levelfactor moreterms | <E>
    // levelfactor ::= calllevel morefactors
    // morefactors ::= * atom morefactors | / atom morefactors | % atom morefactors | <E>
    // atom ::= <number> | <string> callmaybe | <id> callmaybe | (expr) | [expr] | . | .. | <E>
    // callmaybe ::= (expr) | . atom | : expr | <E>

    public Val parse() {
        return flow();
    }

    private Val flow() {
        return moreflow(expr());
    }

    private Val moreflow(Val lval) {
        if (lex.matchOperator(",")) {
            return new Chain(lval, flow());
        } else {
            return lval;
        }
    }

    private Val expr() {
        return morepairs(pairlevel());
    }

    private Val pairlevel() {
        return morepairs(condlevel());
    }

    private Val morepairs(Val lval) {
        if (lex.matchOperator("::")) {
            return morepairs(new Dop(Dop.PAIR, lval, condlevel()));
        } else {
            return lval;
        }
    }

    private Val condlevel() {
        return morecond(levelor());
    }

    private Val morecond(Val lval) {
        if (lex.matchOperator("?")) {
            Val tval = expr();
            if (lex.matchOperator("!!")) {
                return new IfElse(lval, tval, expr());
            } else {
                return new If(lval, tval);
            }
        } else if (lex.matchOperator("?~")) {
            return new While(lval, expr());
        } else if (lex.matchOperator("#")) {
            return new For(lval, expr());
        } else {
            return lval;
        }
    }

    private Val levelor() {
        return moreor(leveland());
    }

    private Val moreor(Val lval) {
        if (lex.matchOperator("||")) {
            return moreor(new Dop(Dop.OR, lval, leveland()));
        } else {
            return lval;
        }
    }

    private Val leveland() {
        return moreand(levelcomp());
    }

    private Val moreand(Val lval) {
        if (lex.matchOperator("&&")) {
            return moreand(new Dop(Dop.AND, lval, levelcomp()));
        } else {
            return lval;
        }
    }

    private Val levelcomp() {
        return morecomp(termlevel());
    }

    private Val morecomp(Val lval) {
        if (lex.matchOperator("=")) {
            return morecomp(new Dop('=', lval, termlevel()));
        } else if (lex.matchOperator("<")) {
            return morecomp(new Dop('<', lval, termlevel()));
        } else if (lex.matchOperator("<=")) {
            return morecomp(new Dop(Dop.LEQ, lval, termlevel()));
        } else if (lex.matchOperator(">")) {
            return morecomp(new Dop('>', lval, termlevel()));
        } else if (lex.matchOperator(">=")) {
            return morecomp(new Dop(Dop.GEQ, lval, termlevel()));
        } else if (lex.matchOperator("<>")) {
            return morecomp(new Dop(Dop.NEQ, lval, termlevel()));
        } else {
            return lval;
        }
    }

    private Val termlevel() {
        return moreterms(factorlevel());
    }

    private Val moreterms(Val lval) {
        if (lex.matchOperator("+")){
            Val rval = factorlevel();
            return moreterms(new Dop('+', lval, rval));
        } else if (lex.matchOperator("-")) {
            Val rval = factorlevel();
            return moreterms(new Dop('-', lval, rval));
        } else {
            return lval;
        }
    }

    private Val factorlevel() {
        return morefactors(atom());
    }

    private Val morefactors(Val lval) {
        if (lex.matchOperator("*")){
            Val rval = atom();
            return morefactors(new Dop('*', lval, rval));
        } else if (lex.matchOperator("/")) {
            Val rval = atom();
            return morefactors(new Dop('/', lval, rval));
        } else if (lex.matchOperator("%")) {
            Val rval = atom();
            return morefactors(new Dop('%', lval, rval));
        } else {
            return lval;
        }
    }

    private Val atom() {
        Token t = lex.nextToken();
        if (t.isOperator("(")) {
            Val lval = flow();
            Token tc = lex.nextToken();
            if (!tc.isOperator(")")) {
                throw new EvalException("syntax error: ) is expected, found "
                        + tc + " instead");
            }
            return lval;
        } else if (t.isOperator("[")) {
            Val lval = flow();
            Token tc = lex.nextToken();
            if (!tc.isOperator("]")) {
                throw new EvalException("syntax error: ] was expected, found ["
                        + tc + "] instead");
            }
            return new Env(lval);
        } else if (t.type == Token.Type.number) {
            return new Num(t.getDouble());
        } else if (t.type == Token.Type.string) {
            return callmaybe(new Str("" + t.value));
        } else if (t.type == Token.Type.id) {
            if (("" + t.value).toLowerCase().equals("nil")) {
                return callmaybe(Nil.NIL);
            }
            return callmaybe(new Id("" + t.value));
        } else if (t.isOperator(".")) {
            return new Scope(atom());
        } else if (t.isOperator("..")) {
            return new Host(atom());
        }
        lex.retToken();
        return Nil.NIL;
    }

    private Val callmaybe(Val fun) {
        if (lex.matchOperator("(")) {
            Val setup = flow();
            if (!lex.matchOperator(")")) {
                throw new EvalException("syntax error: ) was expected");
            }
            return new Call(fun, setup);
        } else if (lex.matchOperator(".")) {
            return new Dop('.', fun, atom());
        } else if (lex.matchOperator(":")) {
            if (fun.getType() != Val.Type.ID) {
                throw new EvalException("identifier is expected, but [" + fun + "] found instead");
            }
            return new Map((Id)fun, expr());
        } else {
            return fun;
        }
    }
}
