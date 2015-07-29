package io.ttl.val;

public interface Val {

    public boolean isAtom();

    public ValType getType();

    public Val eval(Env env);

    public Double evalNum(Env env);

    public String evalStr(Env env);

    public String toTree();
}
