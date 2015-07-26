package io.ttl.val;

import io.ttl.Env;

public class Str extends Nil implements Val {

    protected final String value;

    public Str(String value) {
        this.value = value;
    }

    @Override
    public boolean isAtom() {
        return true;
    }

    @Override
    public ValType getType() {
        return ValType.STR;
    }

    @Override
    public Val eval(Env env) {
        return this;
    }

    @Override
    public String evalStr(Env env) {
        return value;
    }

    @Override
    public Double evalNum(Env env) {
        return Double.parseDouble(value);
    }

    @Override
    public String toString() {
        return "'" + value + "'";
    }
}
