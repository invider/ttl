package io.ttl.val;

import io.ttl.EvalException;

public class Group implements Val {

    protected final Val head, tail;

    public Group(Val head, Val tail) {
        this.head = head;
        this.tail = tail;
    }

    public Val getHead() {
        return head;
    }

    public Val getTail() {
        return tail;
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
    public Val eval(Frame frame) {
        Val res = head.eval(frame);
        if (res.getType() != Type.success) {
            frame.set(res); // index association
        }
        return tail.eval(frame);
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
        return "" + head + ", " + tail;
    }

    @Override
    public String toTree() {
        return "(" + head.toTree() + " " + tail.toTree() + ")";
    }
}
