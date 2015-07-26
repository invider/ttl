package io.ttl.val;

import io.ttl.Env;

public class Chain extends Op implements Val {

    Val head, tail;

    public Chain(Val head, Val tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public Val eval(Env env) {
        head.eval(env);
        return tail.eval(env);
    }

    @Override
    public String toTree() {
        return head.toTree() + ", " + tail.toTree();
    }

    @Override
    public String toString() {
        return "" + head + ", " + tail;
    }
}
