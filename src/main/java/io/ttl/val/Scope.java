package io.ttl.val;

import io.ttl.EvalException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Scope implements Val {

    private Scope parent;

    protected Map<String, Val> map = new ConcurrentHashMap<>();

    public Scope() {
        this.parent = null;
    }

    public Scope(Scope parent) {
        this.parent = parent;
    }

    public Scope getParent() {
        return parent;
    }

    public void setParent(Scope parent) {
        this.parent = parent;
    }

    public void set(String name, Val val) {
        if (name.startsWith("_") && map.containsKey(name)) {
            throw new EvalException("constant [" + name + "] is already defined");
        }
        if (val.getType() != Type.nil) {
            map.put(name, val);
            if (val instanceof Scope) {
                ((Scope) val).setParent(this);
            }
        } else {
            map.remove(name);
        }
    }

    public Val val(String name) {
        Val res = map.get(name);
        if (res == null) {
            if (parent == null) return Nil.NIL;
            else return parent.val(name);
        }
        return res;
    }

    @Override
    public Val eval(Scope scope) {
        return this;
    }

    public Val eval(String src, Scope scope) {
        if (parent == null)
            new EvalException("can't evaluate in this scope");
        return parent.eval(src, scope);
    }

    public Val eval(String src) {
        return eval(src, this);
    }

    public String exec(String src) {
        return "" + eval(src);
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.scope) {
            throw new EvalException(
                "" + t + " was expected, but a scope " + this + " found");
        }
        return this;
    }

    @Override
    public Type getType() {
        return Val.Type.scope;
    }

    @Override
    public boolean isAtom() {
        return true;
    }

    @Override
    public Double evalNum(Scope scope) {
        throw new EvalException("can't evaluate scope to number" + this);
    }

    @Override
    public String evalStr(Scope scope) {
        throw new EvalException("can't evaluate scope to string " + this);
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("[");
        for (String name : map.keySet()) {
            buf.append(name).append(":").append(map.get(name)).append(" ");
        }
        return buf.toString().trim() + "]";
    }

    @Override
    public String toTree() {
        StringBuilder buf = new StringBuilder("[");
        for (String name : map.keySet()) {
            buf.append(name).append(":").append(map.get(name).toTree()).append(" ");
        }
        return buf.toString().trim() + "]";
    }
}
