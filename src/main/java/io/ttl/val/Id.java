package io.ttl.val;

import io.ttl.EvalException;

public class Id implements Val {

    protected String name;

    public Id(String name) {
       this.name = name;
    }

    @Override
    public Type getType() {
        return Type.id;
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.id) {
            throw new EvalException(
                    "" + t + " was expected, but an id ["
                            + name + "] is found");
        }
        return this;
    }

    @Override
    public boolean isAtom() {
        return false;
    }

    @Override
    public Val eval(Frame frame) {
        return frame.val(name);
    }

    @Override
    public Double evalNum(Frame frame) {
        return frame.val(name).evalNum(frame);
    }

    @Override
    public String evalStr(Frame frame) {
        return frame.val(name).evalStr(frame);
    }

    @Override
    public String toString() {
        return "<" + name + ">";
    }

    @Override
    public String toTree() {
        return toString();
    }
}
