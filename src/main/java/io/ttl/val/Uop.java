package io.ttl.val;

import io.ttl.Env;
import io.ttl.EvalException;

public class Uop implements Val {

    private final char op;

    private final Val lval;

    public Uop(char op, Val lval) {
        this.op = op;
        this.lval = lval;
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.op) {
            throw new EvalException(
                    "" + t + " was expected, but an operator [" + op + "] found");
        }
        return this;
    }

    @Override
    public Type getType() {
        return Val.Type.op;
    }

    @Override
    public boolean isAtom() {
        return false;
    }

    @Override
    public Val eval(Env env) {
        switch(op) {
            case '!':
                Val lv = lval.eval(env);
                if (lv.getType() == Type.nil) return Val.TRUE;
                else return Nil.NIL;
            default:
                throw new EvalException("unknown operator: " + op);
        }
    }

    @Override
    public Double evalNum(Env env) {
        return eval(env).expect(Type.num).evalNum(env);
    }

    @Override
    public String evalStr(Env env) {
        return eval(env).expect(Type.string).evalStr(env);
    }

    @Override
    public String toString() {
        return "" + lval + " " + op;
    }

    @Override
    public String toTree() {
        return "(" + op
                + " " + lval.toTree() + ")";
    }
}
