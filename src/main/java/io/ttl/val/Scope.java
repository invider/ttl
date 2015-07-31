package io.ttl.val;

public class Scope extends Env implements Val {

    public Scope(Val eval) {
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
