package io.ttl.val;

import io.ttl.EvalException;

public abstract class SysFun implements Val {

    protected final String name;

    public SysFun() {
        String n = this.getClass().getSimpleName();
        name = n.substring(0, 1).toLowerCase() + n.substring(1, n.length());
    }

    public SysFun(String name) {
        this.name = name;
    }

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

    protected abstract Val syscall(Frame frame);

    @Override
    public Val eval(Frame frame) {
        return syscall(frame);
    }

    @Override
    public Double evalNum(Frame frame) {
        return eval(frame).expect(Type.num).evalNum(frame);
    }

    @Override
    public String evalStr(Frame frame) {
        return eval(frame).expect(Type.string).evalStr(frame);
    }

    @Override
    public boolean eq(Val v, Frame frame) {
        throw new EvalException("can't compare: " + this + " = " + v);
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
