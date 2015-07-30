package io.ttl.val;

import io.ttl.EvalException;

public class Nil implements Val {

    public final static Nil NIL = new Nil();

    public Nil() {}

    @Override
    public boolean isAtom() {
        return true;
    }

    @Override
    public Type getType() {
        return Type.NIL;
    }

    @Override
    public Val eval(Env env) {
        return this;
    }

    @Override
    public Double evalNum(Env env) {
        throw new EvalException("type error: number is expected, but ["
            + this.toString() + "] found instead");
    }

    @Override
    public String evalStr(Env env) {
        throw new RuntimeException("type error: string is expected, but ["
            + this.toString() + "] found instead");
    }

    @Override
    public String toTree() {
        return toString();
    }

    @Override
    public String toString() {
        return "<NIL>";
    }
}
