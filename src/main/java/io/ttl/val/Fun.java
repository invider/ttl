package io.ttl.val;

import io.ttl.Env;

public abstract class Fun extends Nil implements Val{

    @Override
    public boolean isAtom() {
        return false;
    }

    @Override
    public ValType getType() {
        return ValType.FUN;
    }

    @Override
    abstract public Val eval(Env env);

    public Double evalNum(Env env) {
        return eval(env).evalNum(env);
    }

    public String evalStr(Env env) {
        return eval(env).evalStr(env);
    }

    @Override
    public String toString() {
        return "->" + this.getClass().getSimpleName() + "<-";
    }
}
