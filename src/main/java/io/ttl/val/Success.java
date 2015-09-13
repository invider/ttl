package io.ttl.val;

import io.ttl.EvalException;

public class Success implements Val {

    public static final Success SUCCESS = new Success();

    private Success() {}

    @Override
    public Type getType() {
        return Type.success;
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.success) {
            throw new EvalException(
                    "" + t + " was expected, but SUCCESS is found");
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
        throw new EvalException("number is expected, but SUCCESS is found");
    }

    @Override
    public String evalStr(Frame frame) {
        throw new EvalException("str is expected, but SUCCESS is found");
    }

    @Override
    public boolean eq(Val v, Frame frame) {
        return v.getType() == Type.success;
    }

    @Override
    public String toString() {
        return "<SUCCESS>";
    }

    @Override
    public String toTree() {
        return toString();
    }
}
