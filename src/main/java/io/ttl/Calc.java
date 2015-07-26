package io.ttl;

import io.ttl.val.*;

import java.io.Reader;
import java.io.StringReader;

public class Calc extends Env implements Eval {

    @Override
    public String exec(String src) {
        return "" + apply(this, src);
    }

    public Val apply(Env env, String src) {
        src = src.trim();
        if (src.length() == 0) return Nil.NIL;

        Reader reader = new StringReader(src);
        Lex lex = new Lex(reader);
        try {
            Val lval = expr(lex);
            System.out.println("# " + lval);
            System.out.println("% " + lval.toTree());
            return lval.eval(env);
        } catch (EvalException e) {
            e.setSrc(lex.getCurrentLine());
            throw e;
        } catch (Throwable t) {
            throw new EvalException(t, lex.getCurrentLine());
        }
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
    // expr ::= levelor morexpr
    // morexpr ::= ? expr ! expr
    //          | ?~ expr
    //          | *~ expr
    //          | : expr
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
    // atom ::= <number> | <string> | <id> callmaybe | (expr)
    // callmaybe ::= (expr) | <E>

    private Val expr(Lex lex) {
        return morecond(lex, levelor(lex));
    }

    private Val morecond(Lex lex, Val lval) {
        Token t = lex.nextToken();
        if (t.isOperator("?")) {
            Val tval = expr(lex);
            t = lex.nextToken();
            if (t.isOperator("!!")) {
                return new IfElse(lval, tval, expr(lex));
            } else {
                lex.retToken();
                return new If(lval, tval);
            }
        } else if (t.isOperator("?~")) {
            return new While(lval, expr(lex));
        } else if (t.isOperator("#")) {
            return new For(lval, expr(lex));
        } else if (t.isOperator(":")) {
            if (lval.getType() != ValType.ID) {
                throw new EvalException("identifier is expected, but [" + lval + "] found instead");
            }
            return new Map((Id)lval, expr(lex));
        } else {
            lex.retToken();
            return lval;
        }
    }

    private Val levelor(Lex lex) {
        return moreand(lex, leveland(lex));
    }

    private Val moreor(Lex lex, Val lval) {
        Token t = lex.nextToken();
        if (t.isOperator("||")) {
            return moreor(lex, new Dop(Dop.OR, lval, leveland(lex)));
        } else {
            lex.retToken();
            return lval;
        }
    }

    private Val leveland(Lex lex) {
        return moreand(lex, levelcomp(lex));
    }

    private Val moreand(Lex lex, Val lval) {
        Token t = lex.nextToken();
        if (t.isOperator("&&")) {
            return moreand(lex, new Dop(Dop.AND, lval, levelcomp(lex)));
        } else {
            lex.retToken();
            return lval;
        }
    }

    private Val levelcomp(Lex lex) {
        return morecomp(lex, termlevel(lex));
    }

    private Val morecomp(Lex lex, Val lval) {
        Token t = lex.nextToken();
        if (t.isOperator("=")) {
            return morecomp(lex, new Dop('=', lval, termlevel(lex)));
        } else if (t.isOperator("<")) {
            return morecomp(lex, new Dop('<', lval, termlevel(lex)));
        } else if (t.isOperator("<=")) {
            return morecomp(lex, new Dop(Dop.LEQ, lval, termlevel(lex)));
        } else if (t.isOperator(">")) {
            return morecomp(lex, new Dop('>', lval, termlevel(lex)));
        } else if (t.isOperator(">=")) {
            return morecomp(lex, new Dop(Dop.GEQ, lval, termlevel(lex)));
        } else if (t.isOperator("<>")) {
            return morecomp(lex, new Dop(Dop.NEQ, lval, termlevel(lex)));
        } else {
            lex.retToken();
            return lval;
        }
    }

    private Val termlevel(Lex lex) {
        return moreterms(lex, factorlevel(lex));
    }

    private Val moreterms(Lex lex, Val lval) {
        Token t = lex.nextToken();
        if (t.isOperator("+")){
            Val rval = factorlevel(lex);
            return moreterms(lex, new Dop('+', lval, rval));
        } else if (t.isOperator("-")) {
            Val rval = factorlevel(lex);
            return moreterms(lex, new Dop('-', lval, rval));
        } else {
            lex.retToken();
            return lval;
        }
    }

    private Val factorlevel(Lex lex) {
        return morefactors(lex, atom(lex));
    }

    private Val morefactors(Lex lex, Val lval) {
        Token t = lex.nextToken();
        if (t.isOperator("*")){
            Val rval = atom(lex);
            return morefactors(lex, new Dop('*', lval, rval));
        } else if (t.isOperator("/")) {
            Val rval = atom(lex);
            return morefactors(lex, new Dop('/', lval, rval));
        } else if (t.isOperator("%")) {
            Val rval = atom(lex);
            return morefactors(lex, new Dop('%', lval, rval));
        } else {
            lex.retToken();
            return lval;
        }
    }

    private Val atom(Lex lex) {
        Token t = lex.nextToken();
        if (t.isOperator("(")) {
            Val lval = expr(lex);
            Token tc = lex.nextToken();
            if (!tc.isOperator(")")) {
                throw new EvalException("syntax error: unexpected end of expression - ) is expected, found "
                + tc + " instead");
            }
            return lval;
        } else if (t.type == Token.TokenType.number) {
            return new Num(t.getDouble());
        } else if (t.type == Token.TokenType.string) {
            return new Str("" + t.value);
        } else if (t.type == Token.TokenType.id) {
            return callmaybe(lex, new Id("" + t.value));
        }
        lex.retToken();
        return Nil.NIL;
    }

    private Val callmaybe(Lex lex, Id id) {
        Token t = lex.nextToken();
        if (t.isOperator("(")) {
            Val setup = expr(lex);
            t = lex.nextToken();
            if (!t.isOperator(")")) {
                throw new EvalException("syntax error: ) was expected");
            }
            return new Call(id, setup);
        } else {
            lex.retToken();
            return id;
        }
    }
}
