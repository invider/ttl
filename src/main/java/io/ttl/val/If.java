package io.ttl.val;

import io.ttl.EvalException;

public class If implements Val {

    private final Val cval, tval, fval;

    public If(Val cval, Val tval) {
        this.cval = cval;
        this.tval = tval;
        this.fval = null;
    }

    public If(Val cval, Val tval, Val fval) {
        this.cval = cval;
        this.tval = tval;
        this.fval = fval;
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.op) {
            throw new EvalException(
                    "" + t + " was expected, but ? found");
        }
        return this;
    }

    @Override
    public Type getType() {
        return Type.op;
    }

    @Override
    public boolean isAtom() {
        return false;
    }

    @Override
    public Val eval(Scope scope) {
        Val cv = cval.eval(scope);
        if (cv.getType() != Type.nil) {
            return tval.eval(scope);
        } else {
            if (fval == null) return Nil.NIL;
            return fval.eval(scope);
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
        if (fval == null) {
            return "[" + cval + "]? [" + tval + "]";
        } else {
            return "[" + cval + "]? [" + tval + "] !! [" + fval + "]";
        }
    }

    @Override
    public String toTree() {
        if (fval == null) {
            return "(? "
                    + " " + cval.toTree()
                    + " " + tval.toTree() + ")";
        } else {
            return "(? "
                    + " " + cval.toTree()
                    + " " + tval.toTree()
                    + " " + fval.toTree()
                    + ")";
        }
    }
}
