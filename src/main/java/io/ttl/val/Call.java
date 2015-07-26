package io.ttl.val;

import io.ttl.Env;
import io.ttl.EvalException;

public class Call extends Op implements Val {

    private Id id;

    private Val setup;

    public Call(Id id, Val setup) {
        this.id = id;
        this.setup = setup;
    }

    @Override
    public Val eval(Env env) {
        Val fun = env.val(id.name);
        Env frame = new Env(env);
        setup.eval(frame);
        if (fun.getType() == ValType.FUN) {
            return fun.eval(frame);
        } else if (fun.getType() == ValType.STR) {
            return env.apply(frame, fun.evalStr(env));
        } else {
            throw new EvalException("[" + id.name + "] is not a function");
        }
    }

    @Override
    public String toTree() {
        return "(" + id.name + " " + setup.toTree() + ")";
    }

    @Override
    public String toString() {
        return "" + setup + " ->" + id.name + "<-";
    }

}
