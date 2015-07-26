package io.ttl.val;

import io.ttl.Env;

public class Map extends Op implements Val {

    Id id;
    Val rval;

    public Map(Id id, Val rval) {
        this.id = id;
        this.rval = rval;
    }

    @Override
    public Val eval(Env env) {
        Val ra = rval.eval(env);
        env.set(id.name, ra);
        return ra;
    }

    @Override
    public String toTree() {
        return "" + id.toTree() + ": " + rval.toTree();
    }

    @Override
    public String toString() {
        return "" + id + ": " + rval;
    }
}
