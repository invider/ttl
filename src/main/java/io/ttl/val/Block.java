package io.ttl.val;

public class Block extends Scope {

    private Val val;

    public Block(Val val) {
        this.val = val;
    }

    @Override
    public Val eval(Scope scope) {
        Scope newScope = new Scope(scope);
        val.eval(newScope);
        return newScope;
    }
}
