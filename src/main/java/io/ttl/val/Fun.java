package io.ttl.val;

import io.ttl.Env;
import io.ttl.EvalException;

public class Fun implements Val {

    private Val id, params;

    public Fun(Val id, Val params) {
        this.id = id;
        this.params = params;
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.fun) {
            throw new EvalException(
                    "" + t + " was expected, but a function [" + id + "] found");
        }
        return this;
    }

    @Override
    public Type getType() {
        return Val.Type.fun;
    }

    @Override
    public boolean isAtom() {
        return false;
    }

    @Override
    public Val eval(Env env) {
        Val call = id.eval(env);
        if (call.getType() == Type.sys) {
            Env funScope = new Env(env);
            Val pv = params.eval(funScope);
            return call.eval(funScope);
        } else {
            String src = call.expect(Type.string).evalStr(env);
            Env funScope = new Env(env);
            Val pv = params.eval(funScope);
            return funScope.eval(src);
        }
    }


    @Override
    public Double evalNum(Env env) {
        return eval(env).expect(Type.num).evalNum(env);
    }

    @Override
    public String evalStr(Env env) {
        return eval(env).expect(Type.string).evalStr(env);
    }

    @Override
    public String toString() {
        return "" + id + "(" + params + ")";
    }

    @Override
    public String toTree() {
        return "(" + id.toTree() + " " + params.toTree() + ")";
    }
}
