package io.ttl.val;

import io.ttl.Env;
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
    public Val eval(Env env) {
        Val cv = cval.eval(env);
        if (cv.getType() != Type.nil) {
            return tval.eval(env);
        } else {
            if (fval == null) return Nil.NIL;
            return fval.eval(env);
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
