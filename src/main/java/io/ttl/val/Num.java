package io.ttl.val;

import io.ttl.Env;
import io.ttl.EvalException;

public class Num implements Val {

    private final Double val;

    public Num(Double val) {
        this.val = val;
    }

    @Override
    public Type getType() {
        return Type.num;
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.num) {
            throw new EvalException(
                    "" + t + " was expected, but a number ["
                    + val + "] is found");
        }
        return this;
    }

    @Override
    public boolean isAtom() {
        return true;
    }

    @Override
    public Val eval(Env env) {
        return this;
    }

    @Override
    public Double evalNum(Env env) {
        return val;
    }

    @Override
    public String evalStr(Env env) {
        return "" + val;
    }

    @Override
    public String toString() {
        return "" + val;
    }

    @Override
    public String toTree() {
        return toString();
    }
}
