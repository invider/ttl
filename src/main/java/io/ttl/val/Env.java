package io.ttl.val;

import io.ttl.EvalException;

import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

public class Env implements Val {

    protected Env parent;

    protected Val eval;

    private Deque<Val> stack = new ConcurrentLinkedDeque<>();

    private Map<String, Val> map = new ConcurrentHashMap<>();

    public Env() {
        this.eval = Nil.NIL;
    }

    public Env(Env parent) {
        this.parent = parent;
        this.eval = Nil.NIL;
    }

    public Env(Val eval) {
        this.eval = eval;
    }

    public Env(Env parent, Val eval) {
        this.parent = parent;
        this.eval = eval;
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

    public Val parent() {
        if (parent == null) return Nil.NIL;
        return parent;
    }

    public void set(String id, Val val) {
        map.put(id.toLowerCase(), val);
    }

    public Val apply(Env env, String src) {
        return parent.apply(env, src);
    }

    @Override
    public boolean isAtom() {
        return true;
    }

    @Override
    public Type getType() {
        return Type.ENV;
    }

    @Override
    public Val eval(Env env) {
        // TODO maybe need a separate scope val to create new and then evaluate?
        eval.eval(this);
        return this;
    }

    @Override
    public Double evalNum(Env env) {
        throw new EvalException("type error: number is expected, but scope is found");
    }

    @Override
    public String evalStr(Env env) {
        throw new EvalException("type error: string is expected, but scope is found");
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
            if (v.getType() == Val.Type.ENV) {
                buf.append("[...]");
            } else {
                buf.append(v.toString());
            }
        }
        return buf.append(" ]").toString();
    }
}
