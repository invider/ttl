package io.ttl.val;

import io.ttl.EvalException;

public class Group implements Val {

    private Val head, tail;

    public Group(Val head, Val tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.group) {
            throw new EvalException(
                    "" + t + " was expected, but a group [" + this + "] found");
        }
        return this;
    }

    @Override
    public Type getType() {
        return Val.Type.group;
    }

    @Override
    public boolean isAtom() {
        return false;
    }

    @Override
    public Val eval(Scope scope) {
        head.eval(scope);
        return tail.eval(scope);
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
        return "" + head + ", " + tail;
    }

    @Override
    public String toTree() {
        return "(" + head.toTree() + " " + tail.toTree() + ")";
    }
}
