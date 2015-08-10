package io.ttl;

import io.ttl.val.Nil;
import io.ttl.val.Val;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Env {

    private Env parent;

    public Env(Env parent) {
       this.parent = parent;
    }

    protected Map<String, Val> map = new ConcurrentHashMap<>();

    public void set(String name, Val val) {
        if (name.startsWith("_") && map.containsKey(name)) {
            throw new EvalException("constant [" + name + "] is already defined");
        }
        map.put(name, val);
    }

    public Val val(String name) {
        Val res = map.get(name);
        if (res == null) {
            return parent.val(name);
        }
        return res;
    }

    public Val eval(String src, Env env) {
        if (parent == null) new EvalException("can't evaluate in this environment");
        return parent.eval(src, env);
    }

    public Val eval(String src) {
        return eval(src, this);
    }

    public String exec(String src) {
        return "" + eval(src);
    }
}
