package io.ttl.val;

public class If extends Op implements Val {

    private Val cond, tval;

    public If (Val cond, Val tval) {
        this.cond = cond;
        this.tval = tval;
    }

    @Override
    public Val eval(Env env) {
        if (cond.eval(env).getType() == ValType.NIL) return Nil.NIL;
        return tval.eval(env);
    }

    @Override
    public String toTree() {
        return "(? (" + cond.toTree() + ")(" + tval.toTree() + "))";
    }

    @Override
    public String toString() {
        return "[" + cond + "]? [" + tval + "]";
    }
}
