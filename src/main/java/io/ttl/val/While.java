package io.ttl.val;

import io.ttl.EvalException;

public class While implements Val {

    protected final Val condVal, bodyVal;

    public While(Val condVal, Val bodyVal) {
        this.condVal = condVal;
        this.bodyVal = bodyVal;
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.op) {
            throw new EvalException(
                    "" + t + " was expected, but ?~ found");
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
    public Val eval(Frame frame) {
        Val res = Nil.NIL;
        while (condVal.eval(frame).getType() != Val.Type.nil) {
            res = bodyVal.eval(frame);
        }
        return res;
    }

    @Override
    public Double evalNum(Frame frame) {
        return eval(frame).expect(Type.num).evalNum(frame);
    }

    @Override
    public String evalStr(Frame frame) {
        return eval(frame).expect(Type.string).evalStr(frame);
    }

    @Override
    public boolean eq(Val v, Frame frame) {
        throw new EvalException("can't compare: " + this + " = " + v);
    }


    @Override
    public String toString() {
        return "[" + condVal + "]?~ [" + bodyVal + "]";
    }

    @Override
    public String toTree() {
        return "(?~ "
                + " " + condVal.toTree()
                + " " + bodyVal.toTree() + ")";
    }
}
