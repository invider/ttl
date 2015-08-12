package io.ttl;

import io.ttl.val.*;

public class Parser {

    private Scope scope;

    private Lex lex;

    public Parser(Scope scope, Lex lex) {
        this.scope = scope;
        this.lex = lex;
    }

    // BASIC SYNTAX
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

    // FULL SYNTAX
    // flow ::= expr moreflow
    // moreflow  ::= ,flow | <E>
    // expr ::= condlevel
    // condlevel ::= orlevel morecond
    // morecond ::= ? expr
    //          | ? expr !! expr
    //          | ?~ expr
    //          | *~ expr
    //          | <E>
    // orlevel ::= andlevel moreor
    // moreor ::= || andlevel moreor | <E>
    // andlevel :: levelcomp moreand
    // moreand ::= && levelcomp moreand | <E>
    // complevel ::= termlevel morecomp
    // morecomp ::= > termlevel morecomp
    //                | < termlevel morecomp
    //                | >= termlevel morecomp
    //                | <= termlevel morecomp
    //                | = termlevel morecomp
    //                | <> termlevel morecomp
    //                | <E>
    // termlevel ::= factorlevel moreterms
    // moreterms ::= + factorlevel moreterms | - factorlevel moreterms | <E>
    // factorlevel ::= not morefactors
    // morefactors ::= * not morefactors |
    //                 / not morefactors |
    //                 % not morefactors |
    //                 <E>
    // not ::= dot | !dot
    // dot ::= atom moredot
    // moredot ::= . dot | .. dot | <E>
    // atom ::= <number>
    //          | <string> callmaybe
    //          | <id> callmaybe
    //          | (flow)
    //          | [flow]
    //          | .
    //          | ..
    // callmaybe ::= (flow) | :expr | <E>

    public Val parse() {
        return flow();
    }

    private Val flow() {
        return moreflow(expr());
    }

    private Val moreflow(Val lval) {
        Token t = lex.nextToken();
        if (t.matchOperator(",")) {
            return new Group(lval, flow());
        } else {
            lex.returnToken();
            return lval;
        }
    }

    private Val expr() {
        return condlevel();
    }

    private Val condlevel() {
        return morecond(orlevel());
    }

    private Val morecond(Val lval) {
        Token t = lex.nextToken();
        if (t.matchOperator("?")) {
            Val tval = expr();
            t = lex.nextToken();
            if (t.matchOperator("!!")) {
                Val fval = expr();
                return new If(lval, tval, fval);
            } else {
                lex.returnToken();
                return new If(lval, tval);
            }
        } else {
            lex.returnToken();
            return lval;
        }
    }

    private Val orlevel() {
        return moreor(andlevel());
    }

    private Val moreor(Val lval) {
        Token t = lex.nextToken();
        if (t.matchOperator("||")) {
            return moreor(new Op("||", lval, andlevel()));
        } else {
            lex.returnToken();
            return lval;
        }
    }

    private Val andlevel() {
        return moreand(complevel());
    }

    private Val moreand(Val lval) {
        Token t = lex.nextToken();
        if (t.matchOperator("&&")) {
            return moreand(new Op("&&", lval, complevel()));
        } else {
            lex.returnToken();
            return lval;
        }
    }

    private Val complevel() {
        return morecomp(termlevel());
    }

    private Val morecomp(Val lval) {
        Token t = lex.nextToken();
        if (t.matchOperator("<")) {
            return morecomp(new Op("<", lval, termlevel()));
        } else if (t.matchOperator("<=")) {
            return morecomp(new Op("<=", lval, termlevel()));
        } else if (t.matchOperator(">")) {
            return morecomp(new Op(">", lval, termlevel()));
        } else if (t.matchOperator(">=")) {
            return morecomp(new Op(">=", lval, termlevel()));
        } else if (t.matchOperator("=")) {
            return morecomp(new Op("=", lval, termlevel()));
        } else if (t.matchOperator("<>")) {
            return morecomp(new Op("<>", lval, termlevel()));
        } else {
            lex.returnToken();
            return lval;
        }
    }

    private Val termlevel() {
        return moreterms(factorlevel());
    }

    private Val moreterms(Val lval) {
        Token t = lex.nextToken();
        if (t.matchOperator("+")) {
            return moreterms(new Op("+", lval, factorlevel()));
        } else if (t.matchOperator("-")) {
            return moreterms(new Op("-", lval, factorlevel()));
        } else {
            lex.returnToken();
            return lval;
        }
    }

    private Val factorlevel() {
        return morefactors(not());
    }

    private Val morefactors(Val lval) {
        Token t = lex.nextToken();
        if (t.matchOperator("*")) {
            return morefactors(new Op("*", lval, not()));
        } else if (t.matchOperator("/")) {
            return morefactors(new Op("/", lval, not()));
        } else if (t.matchOperator("%")) {
            return morefactors(new Op("%", lval, not()));
        } else {
            lex.returnToken();
            return lval;
        }
    }

    private Val not() {
        Token t = lex.nextToken();
        if (t.matchOperator("!")) {
            return new Uop('!', dot());
        } else {
            lex.returnToken();
            return dot();
        }
    }

    private Val dot() {
        return moredot(atom());
    }

    private Val moredot(Val lval) {
        Token t = lex.nextToken();
        if (t.matchOperator(".")) {
            return new Op(".", lval, dot());
        } else if (t.matchOperator("..")) {
            return new Op("..", lval, dot());
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
            return callmaybe(new Str("" + t.value));
        } else if (t.type == Token.Type.id) {
            return callmaybe(new Id("" + t.value));
        } else if (t.matchOperator("(")) {
            Val v = flow();
            t = lex.nextToken();
            if (!t.matchOperator(")")) {
                throw new EvalException("syntax error: ) was expected");
            }
            return v;
        } else if (t.matchOperator("[")) {
            Val v = flow();
            t = lex.nextToken();
            if (!t.matchOperator("]")) {
                throw new EvalException("syntax error: ] was expected");
            }
            return new Block(v);
        } else if (t.matchOperator(".")) {
            return new Uop('.', expr());
        } else if (t.matchOperator("..")) {
            return new Uop('D', expr());
        } else {
            lex.returnToken();
            return Nil.NIL;
        }
    }

    private Val callmaybe(Val lval) {
        Token t = lex.nextToken();
        if (t.matchOperator(":")) {
            return new Op(":", lval, expr());
        } else if (t.matchOperator("(")) {
            Val rval = flow();
            t = lex.nextToken();
            if (!t.matchOperator(")")) {
                throw new EvalException("lexical error: ) was expected, but ["
                    + t + "] found");
            }
            return new Fun(lval, rval);
        } else {
            lex.returnToken();
            return lval;
        }
    }

}
