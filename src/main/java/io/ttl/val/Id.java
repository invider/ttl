package io.ttl.val;

import io.ttl.EvalException;

public class Id extends Fun implements Val {

    private final boolean scope, host;

    protected final String name;

    public Id(String name) {
        this.name = name.toLowerCase();
        if (this.name.equals("scope")) {
            this.scope = true;
            this.host = false;
        } else if (this.name.equals("host")) {
            this.host = true;
            this.scope = false;
        } else {
            this.host = false;
            this.scope = false;
        }
    }

    @Override
    public boolean isAtom() {
        return false;
    }

    @Override
    public Type getType() {
        return Type.ID;
    }

    @Override
    public Val eval(Env env) {
        if (host) return env.parent();
        if (scope) return env;
        Val res = env.val(name);
        if (res.getType() == Type.NIL) throw new EvalException("no value associated with [" + name + "]");
        return res;
    }

    @Override
    public String toString() {
        return "<" + name + ">";
    }
}
