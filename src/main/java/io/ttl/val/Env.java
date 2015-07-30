package io.ttl.val;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Env implements Val {

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
        map.put(id, val);
    }

    public Val apply(Env env, String src) {
        return parent.apply(env, src);
    }

    @Override
    public boolean isAtom() {
        return true;
    }

    @Override
    public ValType getType() {
        return ValType.ENV;
    }

    @Override
    public Val eval(Env env) {
        return this;
    }

    @Override
    public Double evalNum(Env env) {
        return null;
    }

    @Override
    public String evalStr(Env env) {
        return null;
    }

    @Override
    public String toTree() {
        return toString();
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append("[");
        for(String name: map.keySet()) {
            buf.append(" ").append(name).append(":");
            Val v = map.get(name);
            if (v.getType() == ValType.ENV) {
                buf.append("[...]");
            } else {
                buf.append(v.toString());
            }
        }
        return buf.append(" ]").toString();
    }
}
