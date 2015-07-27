package io.ttl.val;

import io.ttl.Env;
import io.ttl.EvalException;

public class Id implements Val {

    private String name;

    public Id(String name) {
       this.name = name;
    }

    @Override
    public Type getType() {
        return Type.id;
    }

    @Override
    public void expect(Type t) {
        if (t != Type.id) {
            throw new EvalException(
                    "" + t + " was expected, but an id ["
                            + name + "] is found");
        }
    }

    @Override
    public Double evalNum(Env env) {
        Val v = env.val(name);
        v.expect(Type.num);
        return v.evalNum(env);
    }

    @Override
    public String evalStr(Env env) {
        Val v = env.val(name);
        v.expect(Type.string);
        return v.evalStr(env);
    }

    @Override
    public String toString() {
        return "<" + name + ">";
    }
}
