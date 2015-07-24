package io.ttl.val;

import io.ttl.Pile;

public abstract class Fun extends Nil implements Val{

    private final Pile pile;

    public Fun(Pile pile) {
        this.pile = pile;
    }

    @Override
    public ValType getType() {
        return ValType.FUN;
    }

    @Override
    public String eval() {
        return "";
    }

    @Override
    public String toString() {
        return "->" + this.getClass().getSimpleName() + "<-";
    }
}
