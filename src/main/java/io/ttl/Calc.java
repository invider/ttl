package io.ttl;

import io.ttl.val.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Calc implements Eval, Pile {

    private Map<String, Val> map = new ConcurrentHashMap<>();

    private Deque<Val> stack = new ConcurrentLinkedDeque<>();

    @Override
    public String exec(String src) {
        src = src.trim();
        if (src.length() == 0) return "";

        Reader reader = new StringReader(src);
        Lex lex = new Lex(reader);
        try {
            Val lval = expr(lex);
            return "" + lval;
        } catch (EvalException e) {
            e.setSrc(lex.getCurrentLine());
            throw e;
        } catch (Throwable t) {
            throw new EvalException(t, lex.getCurrentLine());
        }
    }

    // expr ::= expr + term | expr - term | term
    // term ::= term * factor | term / factor | term % factor | factor
    // factor ::= <number> | <string> | <id> | (expr)

    // expr ::= term moreterms
    // moreterms ::= + term moreterms | - term moreterms | <E>
    // term ::= factor morefactors
    // morefactors ::= * factor morefactors | / factor morefactors | % factor morefactors | <E>
    // factor ::= <number> | <string> | <id> | (expr)

    private Val expr(Lex lex) {
        Val lval = term(lex);
        return moreterms(lex, lval);
    }

    private Val moreterms(Lex lex, Val lval) {
        Token t = lex.nextToken();
        if (t.isOperator("+")){
            Val rval = term(lex);
            System.out.print("" + lval + " " + rval + " + ");
            if (lval.getType() == ValType.NUM) {
                if (rval.getType() != ValType.NUM) {
                    throw new EvalException(
                        "eval error: number is expected, but got " + rval + " instead");
                }
                return moreterms(lex, new Num(lval.getNum() + rval.getNum()));
            } else if (lval.getType() == ValType.STR) {
                return moreterms(lex, new Str(lval.getStr() + rval.toString()));
            } else if (lval.getType() == ValType.NIL) {
                return moreterms(lex, lval);
            } else {
                throw new EvalException("eval error: operator + can't be applied to " + lval);
            }
        } else if (t.isOperator("-")) {
            Val rval = term(lex);
            System.out.print("" + lval + " " + rval + " - ");
            return moreterms(lex, new Num(lval.getNum() - rval.getNum()));
        } else {
            lex.retToken();
            return lval;
        }
    }

    private Val term(Lex lex) {
        Val lval = factor(lex);
        return morefactors(lex, lval);
    }

    private Val morefactors(Lex lex, Val lval) {
        Token t = lex.nextToken();
        if (t.isOperator("*")){
            Val rval = factor(lex);
            System.out.print("" + lval + " " + rval + " * ");
            return morefactors(lex, new Num(lval.getNum() * rval.getNum()));
        } else if (t.isOperator("/")) {
            Val rval = factor(lex);
            System.out.print("" + lval + " " + rval + " / ");
            return morefactors(lex, new Num(lval.getNum() / rval.getNum()));
        } else if (t.isOperator("%")) {
            Val rval = factor(lex);
            System.out.print("" + lval + " " + rval + " % ");
            return morefactors(lex, new Num(lval.getNum() % rval.getNum()));
        } else {
            lex.retToken();
            return lval;
        }
    }

    private Val factor(Lex lex) {
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
            return Nil.NIL;
        }
        throw new EvalException("syntax error: unexpected end of expression");
    }

    @Override
    public void push(Val val) {
        stack.push(val);
    }

    @Override
    public Val pop() {
        return stack.pop();
    }

    @Override
    public Val peek() {
        return stack.peek();
    }

    @Override
    public Val val(String name) {
        return map.get(name);
    }
}
