package io.ttl.val;

public abstract class Op extends Fun implements Val {

    @Override
    public ValType getType() {
        return ValType.OP;
    }
}
