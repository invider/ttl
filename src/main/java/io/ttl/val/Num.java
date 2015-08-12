package io.ttl.val;

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
    public Val eval(Scope scope) {
        return this;
    }

    @Override
    public Double evalNum(Scope scope) {
        return val;
    }

    @Override
    public String evalStr(Scope scope) {
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
