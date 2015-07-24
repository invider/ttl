package io.ttl.val;

import io.ttl.Pile;

public class Id extends Nil implements Val {

    private final Pile pile;

    private final String value;

    public Id(Pile pile, String value) {
        this.pile = pile;
        this.value = value.toLowerCase();
    }

    @Override
    public ValType getType() {
        return ValType.ID;
    }

    @Override
    public String getStr() {
        return this.value;
    }

    @Override
    public Val getValue() {
        Val res = pile.val(value);
        if (res == null) throw new RuntimeException("no value associated with [" + value + "]");
        return res;
    }

    @Override
    public String eval() {
        return getValue().eval();
    }

    @Override
    public String toString() {
        return "<" + value + ">";
    }
}
