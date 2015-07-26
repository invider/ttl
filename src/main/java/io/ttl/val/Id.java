package io.ttl.val;

import io.ttl.Env;
import io.ttl.EvalException;

public class Id extends Fun implements Val {

    protected final String name;

    public Id(String name) {
        this.name = name.toLowerCase();
    }

    @Override
    public boolean isAtom() {
        return false;
    }

    @Override
    public ValType getType() {
        return ValType.ID;
    }

    @Override
    public Val eval(Env env) {
        Val res = env.val(name);
        if (res.getType() == ValType.NIL) throw new EvalException("no value associated with [" + name + "]");
        return res;
    }

    @Override
    public String toString() {
        return "<" + name + ">";
    }
}
