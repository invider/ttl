package io.ttl.val;

import io.ttl.Env;
import io.ttl.EvalException;

public abstract class SysFun implements Val {

    protected String name;

    public String getName() {
        return name;
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.sys) {
            throw new EvalException(
                    "" + t + " was expected, but a system function [" + this + "] found");
        }
        return this;
    }

    @Override
    public Type getType() {
        return Val.Type.sys;
    }

    @Override
    public boolean isAtom() {
        return false;
    }

    protected abstract Val syscall(Env env);

    @Override
    public Val eval(Env env) {
        return syscall(env);
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
        return "#" + name;
    }

    @Override
    public String toTree() {
        return "(#" + name + ")";
    }
}
