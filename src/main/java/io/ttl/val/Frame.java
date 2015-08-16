package io.ttl.val;

public class Frame extends Env implements Val {

    public Frame(Val eval) {
        this.eval = eval;
    }

    @Override
    public Val eval(Env env) {
        if (eval.getType() == Type.NIL) {
            return env;
        } else {
            return eval.eval(env);
        }
    }
}
