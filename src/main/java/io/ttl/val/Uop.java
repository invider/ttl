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
    public Val eval(Frame frame) {
        switch(op) {
            case '!':
                Val lv = lval.eval(frame);
                if (lv.getType() == Type.nil) return Val.TRUE;
                else return Nil.NIL;
            case '.':
                if(lval.getType() == Type.nil) {
                    return frame;
                }
                return lval.eval(frame);
            case 'D':
                Frame parent = frame.getParent();
                if (parent == null) {
                    throw new EvalException("can't find parent for " + frame);
                }
                if (lval.getType() == Type.nil) {
                    return parent;
                }
                return lval.eval(parent);
            case '@':
                return frame.val(lval.eval(frame)
                        .expect(Type.num)
                        .evalNum(frame).longValue());
            case 'H':
                lv = lval.eval(frame);
                if (lv.getType() != Type.list) {
                    throw new EvalException(
                            "operator :^ can't be applied to [" + lv + "]");
                }
                return ((List)lv).head();
            case 'T':
                lv= lval.eval(frame);
                if (lv.getType() != Type.list) {
                    throw new EvalException(
                            "operator :~ can't be applied to [" + lv + "]");
                }
                return ((List)lv).tail();
            default:
                throw new EvalException("unknown operator: " + op);
        }
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
        return "" + lval + " " + op;
    }

    @Override
    public String toTree() {
        return "(" + op
                + " " + lval.toTree() + ")";
    }
}
