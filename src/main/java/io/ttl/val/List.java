package io.ttl.val;

import io.ttl.EvalException;

public class List extends Group implements Val {

    public List(Val head, Val tail) {
       super(head, tail);
    }

    public Val head() {
        return head;
    }

    public Val tail() {
        return tail;
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
    public boolean isAtom() {
        return true;
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
        throw new EvalException("string is expected, but list is found");
    }

    @Override
    public String toTree() {
        return "(" + head.toTree() + " :: " + tail.toTree() + ")";
    }

    @Override
    public String toString() {
        return "{" + head + " :: " + tail + "}";
    }
}
