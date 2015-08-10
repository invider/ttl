package io.ttl.val;

import io.ttl.Env;
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
    public Val eval(Env env) {
        head.eval(env);
        return tail.eval(env);
    }

    @Override
    public Double evalNum(Env env) {
        return eval(env).expect(Type.num).evalNum(env);
    }

    @Override
    public String evalStr(Env env) {
        return eval(env).expect(Type.string).evalStr(env);
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
