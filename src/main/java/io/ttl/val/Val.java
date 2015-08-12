package io.ttl.val;

public interface Val {

    public static final Val TRUE = new Num(1d);

    public enum Type {
        nil, num, string, id, op, fun, sys, group, scope
    }

    public Val expect(Type t);

    public Type getType();

    public boolean isAtom();

    public Val eval(Scope scope);

    public Double evalNum(Scope scope);

    public String evalStr(Scope scope);

    public String toTree();
}
