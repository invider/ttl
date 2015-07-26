package io.ttl.val;

import io.ttl.Env;

public class IfElse extends Op implements Val {

    private Val cond, tval, fval;

    public IfElse(Val cond, Val tval, Val fval) {
        this.cond = cond;
        this.tval = tval;
        this.fval = fval;
    }

    @Override
    public Val eval(Env env) {
        if (cond.eval(env).getType() == ValType.NIL) return fval.eval(env);
        return tval.eval(env);
    }

    @Override
    public String toTree() {
        return "(? (" + cond.toTree() + ")(" + tval.toTree() + ")(" + fval.toTree() + "))";
    }

    @Override
    public String toString() {
        return "[" + cond + "]? [" + tval + "]!![" + fval + "]";
    }
}
