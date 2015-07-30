package io.ttl.val;

public interface Val {

    enum Type {
        NIL, NUM, STR, ID, FUN, OP, PAIR, ENV
    }

    public boolean isAtom();

    public Type getType();

    public Val eval(Env env);

    public Double evalNum(Env env);

    public String evalStr(Env env);

    public String toTree();

}
