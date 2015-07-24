package io.ttl.val;

public class Num extends Nil implements Val {

    private final Double value;

    public Num(Double value) {
        this.value = value;
    }

    @Override
    public ValType getType() {
        return ValType.NUM;
    }

    @Override
    public Double getNum() {
        return value;
    }

    @Override
    public String eval() {
        return toString();
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
