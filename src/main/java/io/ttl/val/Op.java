package io.ttl.val;

import io.ttl.EvalException;

public class Op implements Val {

    private String op;

    private Val lval, rval;

    public Op(String op, Val lval, Val rval) {
        this.op = op;
        this.lval = lval;
        this.rval = rval;
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

    private Val comp(Frame frame) {
        Val lv = lval.eval(frame);
        if (lv.getType() == Type.num) {
            double ld = lv.evalNum(frame);
            double rd = rval.evalNum(frame);
            switch(op) {
                case "<": return (ld < rd)? Val.TRUE : Nil.NIL;
                case "<=": return (ld <= rd)? Val.TRUE : Nil.NIL;
                case ">": return (ld > rd)? Val.TRUE : Nil.NIL;
                case ">=": return (ld >= rd)? Val.TRUE : Nil.NIL;
                case "=": return (ld == rd)? Val.TRUE : Nil.NIL;
                case "<>": return (ld != rd)? Val.TRUE : Nil.NIL;
            }
        } else if (lv.getType() == Type.string) {
            String ls = lv.evalStr(frame);
            String rs = rval.evalStr(frame);
            switch(op) {
                case "<": return (ls.compareTo(rs) < 0)? Val.TRUE : Nil.NIL;
                case "<=": return (ls.compareTo(rs) <= 0)? Val.TRUE : Nil.NIL;
                case ">": return (ls.compareTo(rs) > 0)? Val.TRUE : Nil.NIL;
                case ">=": return (ls.compareTo(rs) >= 0)? Val.TRUE : Nil.NIL;
                case "=": return (ls.equals(rs))? Val.TRUE : Nil.NIL;
                case "<>": return (!ls.equals(rs))? Val.TRUE : Nil.NIL;
            }
        } else {
            throw new EvalException("unexpected type for comparison: ["
                    + lval + "]");
        }
        return Nil.NIL;
    }

    @Override
    public Val eval(Frame frame) {
        switch (op) {
            case "+":
                Val lres = lval.eval(frame);
                if (lres.getType() == Type.string) {
                    return new Str(lres.evalStr(frame) + rval.evalStr(frame));
                } else if (lres.getType() == Type.num) {
                    return new Num(lres.evalNum(frame) + rval.evalNum(frame));
                } else if (lres.getType() == Type.nil) {
                    return Nil.NIL;
                } else {
                   throw new EvalException("wrong operands: "
                        + lval.toString() + " + " + rval.toString());
                }
            case "-":
                return new Num(lval.evalNum(frame) - rval.evalNum(frame));
            case "*":
                return new Num(lval.evalNum(frame) * rval.evalNum(frame));
            case "/":
                return new Num(lval.evalNum(frame) / rval.evalNum(frame));
            case "%":
                return new Num(lval.evalNum(frame) % rval.evalNum(frame));
            case ":":
                if (lval.getType() == Type.id) {
                    Id id = (Id)lval;
                    Val val = rval.eval(frame);
                    frame.set(id.name, val);
                    return Success.SUCCESS;
                } else {
                    try {
                        long i = lval.evalNum(frame).longValue();
                        Val val = rval.eval(frame);
                        frame.set(i, val);
                        return Success.SUCCESS;
                    } catch (EvalException e) {
                        new EvalException("id or number is expected", e);
                    }
                }
            case "<":case "<=":case ">":case ">=":
            case "=":case "<>":
                return comp(frame);
            case "&&":
                Val lp = lval.eval(frame);
                if (lp.getType() == Type.nil) return Nil.NIL;
                Val rp = rval.eval(frame);
                if (rp.getType() == Type.nil) return Nil.NIL;
                return Val.TRUE;
            case "||":
                lp = lval.eval(frame);
                if (lp.getType() != Type.nil) return Val.TRUE;
                rp = rval.eval(frame);
                if (rp.getType() != Type.nil) return Val.TRUE;
                return Nil.NIL;
            case ".":
                lres = lval.eval(frame);
                if (lres.getType() != Type.frame) {
                    throw new EvalException("operator . can't be applied to [" + lres + "]");
                }
                Frame context = (Frame)lres;
                return rval.eval(context);
            case "..":
                lres = lval.eval(frame);
                if (lres.getType() != Type.frame) {
                    throw new EvalException(
                            "operator . can't be applied to [" + lres + "]");
                }
                context = ((Frame)lres).getParent();
                if (context == null) {
                    throw new EvalException("no parent frame found for " + lres);
                }
                return rval.eval(context);
            case "::":
                return new List(lval.eval(frame), rval.eval(frame));
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

    public boolean matchOperator(String op) {
        return this.op.equals(op);
    }

    @Override
    public String toString() {
        return "" + lval + " " + rval + " " + op;
    }

    @Override
    public String toTree() {
        return "(" + op
                + " " + lval.toTree()
                + " " + rval.toTree() + ")";
    }
}
