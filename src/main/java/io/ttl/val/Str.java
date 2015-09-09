package io.ttl.val;

import io.ttl.EvalException;

public class Str implements Val {

    private String val;

    public Str(String val) {
        this.val = val;
    }

    @Override
    public Type getType() {
        return Type.string;
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.string) {
            throw new EvalException(
                    "" + t + " was expected, but a string '"
                            + val + "' is found");
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
        try {
            return Double.parseDouble(val.trim());
        } catch (NumberFormatException e) {
            throw new EvalException("number is expected, but string '"
                + val + "' is found", e);
        }
    }

    @Override
    public String evalStr(Frame frame) {
        return val;
    }

    @Override
    public String toString() {
        return val;
    }

    @Override
    public String toTree() {
        return toString();
    }
}
