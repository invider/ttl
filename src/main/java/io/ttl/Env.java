package io.ttl;

import io.ttl.val.Nil;
import io.ttl.val.Val;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Env {

    private Map<String, Val> map = new ConcurrentHashMap<>();

    public void set(String name, Val val) {
        map.put(name, val);
    }

    public Val val(String name) {
        Val res = map.get(name);
        if (res == null) return Nil.NIL;
        return res;
    }

}
