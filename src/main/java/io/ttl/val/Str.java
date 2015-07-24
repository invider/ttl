package io.ttl.val;

public class Str extends Nil implements Val {

    private final String value;

    public Str(String value) {
        this.value = value;
    }

    @Override
    public ValType getType() {
        return ValType.STR;
    }

    @Override
    public String getStr() {
        return value;
    }

    @Override
    public String eval() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
