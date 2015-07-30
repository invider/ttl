package io.ttl.val;

public abstract class Op extends Fun implements Val {

    @Override
    public Type getType() {
        return Type.OP;
    }
}
