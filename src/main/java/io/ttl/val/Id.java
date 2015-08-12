package io.ttl.val;

import io.ttl.EvalException;

public class Id implements Val {

    protected String name;

    public Id(String name) {
       this.name = name;
    }

    @Override
    public Type getType() {
        return Type.id;
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.id) {
            throw new EvalException(
                    "" + t + " was expected, but an id ["
                            + name + "] is found");
        }
        return this;
    }

    @Override
    public boolean isAtom() {
        return false;
    }

    @Override
    public Val eval(Scope scope) {
        return scope.val(name);
    }

    @Override
    public Double evalNum(Scope scope) {
        Val v = scope.val(name);
        v.expect(Type.num);
        return v.evalNum(scope);
    }

    @Override
    public String evalStr(Scope scope) {
        Val v = scope.val(name);
        v.expect(Type.string);
        return v.evalStr(scope);
    }

    @Override
    public String toString() {
        return "<" + name + ">";
    }

    @Override
    public String toTree() {
        return toString();
    }
}
