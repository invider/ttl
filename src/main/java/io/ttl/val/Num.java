package io.ttl.val;

import io.ttl.Env;
import io.ttl.EvalException;

public class Num implements Val {

    private Double val;

    public Num(Double val) {
        this.val = val;
    }

    @Override
    public Type getType() {
        return Type.num;
    }

    @Override
    public void expect(Type t) {
        if (t != Type.num) {
            throw new EvalException(
                    "" + t + " was expected, but a number ["
                    + val + "] is found");
        }
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
}
