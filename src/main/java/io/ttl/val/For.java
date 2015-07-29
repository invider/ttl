package io.ttl.val;

public class For extends Op implements Val {

    private Val cond, ival;

    public For(Val cond, Val ival) {
        this.cond = cond;
        this.ival = ival;
    }

    @Override
    public Val eval(Env env) {
        Val res = Nil.NIL;
        int iter = (int)Math.round(cond.eval(env).evalNum(env));
        for (;iter > 0; iter--) {
            res = ival.eval(env);
        }
        return res;
    }

    @Override
    public String toTree() {
        return "(# (" + cond.toTree() + ")(" + ival.toTree() + "))";
    }

    @Override
    public String toString() {
        return "[" + cond + "]#[" + ival + "]";
    }
}
