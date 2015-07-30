package io.ttl.val;

public class While extends Op implements Val {

    private Val cond, ival;

    public While(Val cond, Val ival) {
        this.cond = cond;
        this.ival = ival;
    }

    @Override
    public Val eval(Env env) {
        Val res = Nil.NIL;
        while(cond.eval(env).getType() != Type.NIL) {
            res = ival.eval(env);
        }
        return res;
    }

    @Override
    public String toTree() {
        return "(?~ (" + cond.toTree() + ")(" + ival.toTree() + "))";
    }

    @Override
    public String toString() {
        return "[" + cond + "]?~ [" + ival + "]";
    }
}
