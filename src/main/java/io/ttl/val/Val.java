package io.ttl.val;

import io.ttl.Env;

public interface Val {

    public static final Val TRUE = new Num(1d);

    public enum Type {
        nil, num, string, id, op
    }

    public Val expect(Type t);

    public Type getType();

    public boolean isAtom();

    public Val eval(Env env);

    public Double evalNum(Env env);

    public String evalStr(Env env);

    public String toTree();
}
