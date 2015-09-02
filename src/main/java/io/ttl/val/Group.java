package io.ttl.val;

import io.ttl.EvalException;

public class Group implements Val {

    protected boolean isLastAssociated = false;

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

    public boolean isLastAssociated() {
        return isLastAssociated;
    }

    public void setIsLastAssociated(boolean isLastAssociated) {
        this.isLastAssociated = isLastAssociated;
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
        if (head.getType() == Type.op && ((Op)head).matchOperator(":")) {
            head.eval(frame); // name association side effect
        } else {
            frame.set(head.eval(frame)); // index association
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
    public String toString() {
        return "" + head + ", " + tail;
    }

    @Override
    public String toTree() {
        return "(" + head.toTree() + " " + tail.toTree() + ")";
    }
}
