package io.ttl.val;

public class Nil implements Val {

    public final static Nil NIL = new Nil();

    public Nil() {}

    @Override
    public ValType getType() {
        return ValType.NIL;
    }

    @Override
    public Double getNum() {
        throw new RuntimeException("wrong value type - not a number");
    }

    @Override
    public String getStr() {
        throw new RuntimeException("wrong value type - not a string");
    }

    @Override
    public Val getValue() {
        throw new RuntimeException("wrong value type - not an identifier");
    }

    @Override
    public String eval() {
        return "";
    }

    @Override
    public String toString() {
        return "<NIL>";
    }
}
