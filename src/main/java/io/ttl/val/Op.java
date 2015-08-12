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

    private Val comp(Scope scope) {
        if (lval.getType() == Type.num) {
            double ld = lval.evalNum(scope);
            double rd = rval.evalNum(scope);
            switch(op) {
                case "<": return (ld < rd)? Val.TRUE : Nil.NIL;
                case "<=": return (ld <= rd)? Val.TRUE : Nil.NIL;
                case ">": return (ld > rd)? Val.TRUE : Nil.NIL;
                case ">=": return (ld >= rd)? Val.TRUE : Nil.NIL;
                case "=": return (ld == rd)? Val.TRUE : Nil.NIL;
                case "<>": return (ld != rd)? Val.TRUE : Nil.NIL;
            }
        } else if (lval.getType() == Type.string) {
            String ls = lval.evalStr(scope);
            String rs = rval.evalStr(scope);
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
    public Val eval(Scope scope) {
        switch (op) {
            case "+":
                Val lres = lval.eval(scope);
                if (lres.getType() == Type.string) {
                    return new Str(lres.evalStr(scope) + rval.evalStr(scope));
                } else if (lres.getType() == Type.num) {
                    return new Num(lres.evalNum(scope) + rval.evalNum(scope));
                } else if (lres.getType() == Type.nil) {
                    return Nil.NIL;
                } else {
                   throw new EvalException("wrong operands: "
                        + lval.toString() + " + " + rval.toString());
                }
            case "-":
                return new Num(lval.evalNum(scope) - rval.evalNum(scope));
            case "*":
                return new Num(lval.evalNum(scope) * rval.evalNum(scope));
            case "/":
                return new Num(lval.evalNum(scope) / rval.evalNum(scope));
            case "%":
                return new Num(lval.evalNum(scope) % rval.evalNum(scope));
            case ":":
                Id id = (Id)lval.expect(Type.id);
                Val val = rval.eval(scope);
                scope.set(id.name, val);
                return val;
            case "<":case "<=":case ">":case ">=":
            case "=":case "<>":
                return comp(scope);
            case "&&":
                Val lp = lval.eval(scope);
                if (lp.getType() == Type.nil) return Nil.NIL;
                Val rp = rval.eval(scope);
                if (rp.getType() == Type.nil) return Nil.NIL;
                return Val.TRUE;
            case "||":
                lp = lval.eval(scope);
                if (lp.getType() != Type.nil) return Val.TRUE;
                rp = rval.eval(scope);
                if (rp.getType() != Type.nil) return Val.TRUE;
                return Nil.NIL;
            case ".":
                lres = lval.eval(scope);
                if (lres.getType() != Type.scope) {
                    throw new EvalException("operator . can't be applied to [" + lres + "]");
                }
                Scope context = (Scope)lres;
                return rval.eval(context);
            case "..":
                lres = lval.eval(scope);
                if (lres.getType() != Type.scope) {
                    throw new EvalException("operator . can't be applied to [" + lres + "]");
                }
                context = ((Scope)lres).getParent();
                if (context == null) {
                    throw new EvalException("no parent scope found for " + lres);
                }
                return rval.eval(context);
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
        return "" + lval + " " + rval + " " + op;
    }

    @Override
    public String toTree() {
        return "(" + op
                + " " + lval.toTree()
                + " " + rval.toTree() + ")";
    }
}
