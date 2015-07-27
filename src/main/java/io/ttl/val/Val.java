package io.ttl.val;

import io.ttl.Env;

public interface Val {

    public enum Type {
        nil, num, string, id
    }

    public void expect(Type t);

    public Type getType();

    public Double evalNum(Env env);

    public String evalStr(Env env);

}
