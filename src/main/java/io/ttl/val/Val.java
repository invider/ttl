package io.ttl.val;

public interface Val {

    public static final Val TRUE = new Num(1d);

    public enum Type {
        nil, success, num, string, id, op, fun, sys, group, frame, list
    }

    public Val expect(Type t);

    public Type getType();

    public boolean isAtom();

    public Val eval(Frame frame);

    public Double evalNum(Frame frame);

    public String evalStr(Frame frame);

    public boolean eq(Val v, Frame frame);

    public String toTree();
}
