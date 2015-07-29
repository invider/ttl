package io.ttl.val;

import io.ttl.EvalException;

public class Call extends Op implements Val {

    private Val id;

    private Val setup;

    public Call(Val id, Val setup) {
        if (id.getType() != ValType.ID
                && id.getType() != ValType.STR
                && id.getType() != ValType.NIL) {
            throw new EvalException("id or string value is expected, but [" + id + "] is found");
        }
        this.id = id;
        this.setup = setup;
    }

    @Override
    public Val eval(Env env) {
        if (id.getType() == ValType.ID) {
            Val fun = id.eval(env);
            Env frame = new Env(env);
            setup.eval(frame);
            if (fun.getType() == ValType.FUN) {
                return fun.eval(frame);
            } else if (fun.getType() == ValType.STR) {
                return env.apply(frame, fun.evalStr(env));
            } else {
                throw new EvalException("[" + ((Id) id).name + "] is not a function");
            }
        } else if (id.getType() == ValType.NIL) {
            return env;
        } else {
            // try to eval function body and apply parameters to that
            String code = id.evalStr(env);
            Env frame = new Env(env);
            setup.eval(frame);
            return env.apply(frame, code);
        }
    }

    @Override
    public String toTree() {
        return "(" + id.toTree() + " " + setup.toTree() + ")";
    }

    @Override
    public String toString() {
        return "" + setup + " ->" + id + "<-";
    }

}
