package io.ttl.val;

import io.ttl.Env;
import io.ttl.EvalException;

public class Str implements Val {

    private String val;

    public Str(String val) {
        this.val = val;
    }

    @Override
    public Type getType() {
        return Type.string;
    }

    @Override
    public void expect(Type t) {
        if (t != Type.string) {
            throw new EvalException(
                    "" + t + " was expected, but a string '"
                            + val + "' is found");
        }
    }

    @Override
    public Double evalNum(Env env) {
        return Double.parseDouble(val.trim());
    }

    @Override
    public String evalStr(Env env) {
        return val;
    }

    @Override
    public String toString() {
        return "'" + val + "'";
    }
}
