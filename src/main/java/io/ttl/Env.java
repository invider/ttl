package io.ttl;

import io.ttl.val.Nil;
import io.ttl.val.Val;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Env {

    private Env parent;

    private Deque<Val> stack = new ConcurrentLinkedDeque<>();

    private Map<String, Val> map = new ConcurrentHashMap<>();

    public Env() {}

    public Env(Env parent) {
        this.parent = parent;
    }

    public void push(Val val) {
        stack.push(val);
    }

    public Val pop() {
        return stack.pop();
    }

    public Val peek() {
        return stack.peek();
    }

    public Val val(String id) {
        Val val = map.get(id);
        if (val == null) {
            if (parent == null) return Nil.NIL;
            return parent.val(id);
        }
        return val;
    }

    public void set(String id, Val val) {
        System.out.println("++ " + id + ": " + val);
        map.put(id, val);
    }

    public Val apply(Env env, String src) {
        return parent.apply(env, src);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for (String id: map.keySet()) {
            buf.append(id + ": " + map.get(id) + "\n");
        }
        return buf.toString();
    }
}
