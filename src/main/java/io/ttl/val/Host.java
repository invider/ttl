package io.ttl.val;

public class Host extends Env implements Val {

    public Host(Val eval) {
        this.eval = eval;
    }

    @Override
    public Val eval(Env env) {
        if (eval.getType() == Type.NIL) {
            return env.parent();
        }
        Val host = env.parent();
        if (host.getType() == Type.ENV) {
            return eval.eval((Env)host);
        }
        return eval.eval(new Env());
    }
}
