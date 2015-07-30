package io.ttl.val;

public class Pair extends Nil implements Val {

    private Val lval, rval;

    public Pair(Val lval, Val rval) {
        this.lval = lval;
        this.rval = rval;
    }

    @Override
    public boolean isAtom() {
        return true;
    }

    @Override
    public Type getType() {
        return Type.PAIR;
    }

    @Override
    public Val eval(Env env) {
        return this;
    }

    @Override
    public String toTree() {
        return "( " + lval.toTree() + rval.toTree() + " )";
    }

    @Override
    public String toString() {
        return "" + lval + " -> " + rval;
    }
}
