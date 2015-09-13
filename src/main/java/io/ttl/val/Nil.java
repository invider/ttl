package io.ttl.val;

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
    public Val eval(Frame frame) {
        return this;
    }

    @Override
    public Double evalNum(Frame frame) {
        throw new EvalException("number is expected, but NIL is found");
    }

    @Override
    public String evalStr(Frame frame) {
        throw new EvalException("str is expected, but NIL is found");
    }

    @Override
    public boolean eq(Val v, Frame frame) {
        return v.getType() == Type.nil;
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
