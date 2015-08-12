package io.ttl.val;

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

    protected abstract Val syscall(Scope scope);

    @Override
    public Val eval(Scope scope) {
        return syscall(scope);
    }

    @Override
    public Double evalNum(Scope scope) {
        return eval(scope).expect(Type.num).evalNum(scope);
    }

    @Override
    public String evalStr(Scope scope) {
        return eval(scope).expect(Type.string).evalStr(scope);
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
