package io.ttl.val;

import io.ttl.Env;
import io.ttl.EvalException;

public class Nil implements Val {

    public static final Nil NIL = new Nil();

    private Nil(){};

    @Override
    public Type getType() {
        return Type.nil;
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.nil) {
            throw new EvalException(
                    "" + t + " was expected, but NIL is found");
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
        throw new EvalException("number is expected, but NIL is found");
    }

    @Override
    public String evalStr(Env env) {
        throw new EvalException("string is expected, but NIL is found");
    }

    @Override
    public String toString() {
        return "<NIL>";
    }

    @Override
    public String toTree() {
        return toString();
    }
}
