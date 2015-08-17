package io.ttl.val;

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
    public Val eval(Scope scope) {
        Val call = id.eval(scope);
        if (call.getType() == Type.sys) {
            Scope funScope = new Scope(scope);
            Val pv = params.eval(funScope);
            return call.eval(funScope);
        } else {
            if (call.getType() != Type.string) {
                throw new EvalException("function body was expected, but ["
                        + call + "] is found");
            }
            String src = call.evalStr(scope);
            Scope funScope = new Scope(scope);
            Val pv = params.eval(funScope);
            return funScope.eval(src);
        }
    }


    @Override
    public Double evalNum(Scope scope) {
        return eval(scope).expect(Type.num).evalNum(scope);
    }

    @Override
    public String evalStr(Scope scope) {
        return eval(scope).expect(Type.string).evalStr(scope);
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
