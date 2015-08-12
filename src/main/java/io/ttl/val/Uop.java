package io.ttl.val;

import io.ttl.EvalException;

public class Uop implements Val {

    private final char op;

    private final Val lval;

    public Uop(char op, Val lval) {
        this.op = op;
        this.lval = lval;
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.op) {
            throw new EvalException(
                    "" + t + " was expected, but an operator [" + op + "] found");
        }
        return this;
    }

    @Override
    public Type getType() {
        return Val.Type.op;
    }

    @Override
    public boolean isAtom() {
        return false;
    }

    @Override
    public Val eval(Scope scope) {
        switch(op) {
            case '!':
                Val lv = lval.eval(scope);
                if (lv.getType() == Type.nil) return Val.TRUE;
                else return Nil.NIL;
            case '.':
                if(lval.getType() == Type.nil) {
                    return scope;
                }
                return lval.eval(scope);
            case 'D':
                Scope parent = scope.getParent();
                if (parent == null) {
                    throw new EvalException("can't find parent for " + scope);
                }
                if (lval.getType() == Type.nil) {
                    return parent;
                }
                return lval.eval(parent);
            default:
                throw new EvalException("unknown operator: " + op);
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
        return "" + lval + " " + op;
    }

    @Override
    public String toTree() {
        return "(" + op
                + " " + lval.toTree() + ")";
    }
}
