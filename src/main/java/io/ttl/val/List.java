package io.ttl.val;

import io.ttl.EvalException;

public class List implements Val {

    private Val lval, rval;

    public List(Val lval, Val rval) {
        this.lval = lval;
        this.rval = rval;
    }

    @Override
    public boolean isAtom() {
        return true;
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.list) {
            throw new EvalException(
                "" + t + " was expected, but a list [" + this + "] found");
        }
        return this;
    }

    @Override
    public Type getType() {
        return Type.list;
    }

    @Override
    public Val eval(Frame frame) {
        return this;
    }

    @Override
    public Double evalNum(Frame frame) {
        throw new EvalException("number is expected, but list is found");
    }

    @Override
    public String evalStr(Frame frame) {
        throw new EvalException("string is expected, but lilst is found");
    }

    @Override
    public String toTree() {
        return "( " + lval.toTree() + rval.toTree() + " )";
    }

    @Override
    public String toString() {
        return "" + lval + " -> " + rval;
    }
}
