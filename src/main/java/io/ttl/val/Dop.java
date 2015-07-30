package io.ttl.val;

import io.ttl.EvalException;

public class Dop extends Fun implements Val {

    public static final char NEQ = 1;
    public static final char LEQ = 2;
    public static final char GEQ = 3;
    public static final char OR  = 4;
    public static final char AND = 5;
    public static final char PAIR = 6;

    private char op;

    private Val lval, rval;

    public Dop(char op, Val lval, Val rval) {
        this.op = op;
        this.lval = lval;
        this.rval = rval;
    }

    @Override
    public boolean isAtom() {
        return false;
    }

    @Override
    public Type getType() {
        return Type.OP;
    }

    public Val eval(Env env) {
        Val la, ra;
        Double ld;
        switch(op) {
            case '+':
                la = lval.eval(env);
                if (la.getType() == Type.NUM) {
                    ra = rval.eval(env);
                    if (ra.getType() == Type.NIL) return Nil.NIL;
                    return new Num(lval.evalNum(env) + rval.evalNum(env));
                } else if (la.getType() == Type.STR) {
                    ra = rval.eval(env);
                    if (ra.getType() == Type.NIL) {
                        return new Str(lval.evalStr(env) + "<NIL>");
                    }
                    return new Str(lval.evalStr(env) + rval.evalStr(env));
                } else if (la.getType() == Type.NIL) {
                    return Nil.NIL;
                }
            case '-':
                la = lval.eval(env);
                if (la.getType() == Type.NIL) return Nil.NIL;
                ld = la.evalNum(env);
                ra = rval.eval(env);
                if (ra.getType() == Type.NIL) return Nil.NIL;
                return new Num(ld - ra.evalNum(env));
            case '*':
                la = lval.eval(env);
                if (la.getType() == Type.NIL) return Nil.NIL;
                ld = la.evalNum(env);
                ra = rval.eval(env);
                if (ra.getType() == Type.NIL) return Nil.NIL;
                return new Num(ld * ra.evalNum(env));
            case '/':
                la = lval.eval(env);
                if (la.getType() == Type.NIL) return Nil.NIL;
                ld = la.evalNum(env);
                ra = rval.eval(env);
                if (ra.getType() == Type.NIL) return Nil.NIL;
                return new Num(ld / ra.evalNum(env));
            case '%':
                la = lval.eval(env);
                if (la.getType() == Type.NIL) return Nil.NIL;
                ld = la.evalNum(env);
                ra = rval.eval(env);
                if (ra.getType() == Type.NIL) return Nil.NIL;
                return new Num(ld % ra.evalNum(env));
            case '=':
                la = lval.eval(env);
                ra = rval.eval(env);
                if (la.equals(ra)) return Num.TRUE;
                else return Nil.NIL;
            case NEQ:
                la = lval.eval(env);
                ra = rval.eval(env);
                if (la.equals(ra)) return Nil.NIL;
                else return Num.TRUE;
            case '<':
                la = lval.eval(env);
                ra = rval.eval(env);
                if (la.getType() == Type.NUM || la.getType() == Type.NUM) {
                    if (la.evalNum(env) < ra.evalNum(env)) return Num.TRUE;
                    else return Nil.NIL;
                } else if (la.getType() == Type.STR || ra.getType() == Type.STR) {
                    if (la.evalStr(env).compareTo(ra.evalStr(env)) < 0) return Num.TRUE;
                    return Nil.NIL;
                } else {
                    throw new EvalException("can't apply operation < to ["
                    + la + "] and [" + ra + "]");
                }
            case '>':
                la = lval.eval(env);
                ra = rval.eval(env);
                if (la.getType() == Type.NUM || la.getType() == Type.NUM) {
                    if (la.evalNum(env) > ra.evalNum(env)) return Num.TRUE;
                    else return Nil.NIL;
                } else if (la.getType() == Type.STR || ra.getType() == Type.STR) {
                    if (la.evalStr(env).compareTo(ra.evalStr(env)) > 0) return Num.TRUE;
                    return Nil.NIL;
                } else {
                    throw new EvalException("can't apply operation > to ["
                            + la + "] and [" + ra + "]");
                }
            case LEQ:
                la = lval.eval(env);
                ra = rval.eval(env);
                if (la.getType() == Type.NUM || la.getType() == Type.NUM) {
                    if (la.evalNum(env) <= ra.evalNum(env)) return Num.TRUE;
                    else return Nil.NIL;
                } else if (la.getType() == Type.STR || ra.getType() == Type.STR) {
                    if (la.evalStr(env).compareTo(ra.evalStr(env)) <= 0) return Num.TRUE;
                    return Nil.NIL;
                } else {
                    throw new EvalException("can't apply operation <= to ["
                            + la + "] and [" + ra + "]");
                }
            case GEQ:
                la = lval.eval(env);
                ra = rval.eval(env);
                if (la.getType() == Type.NUM || la.getType() == Type.NUM) {
                    if (la.evalNum(env) >= ra.evalNum(env)) return Num.TRUE;
                    else return Nil.NIL;
                } else if (la.getType() == Type.STR || ra.getType() == Type.STR) {
                    if (la.evalStr(env).compareTo(ra.evalStr(env)) >= 0) return Num.TRUE;
                    return Nil.NIL;
                } else {
                    throw new EvalException("can't apply operation >= to ["
                            + la + "] and [" + ra + "]");
                }
            case OR:
                la = lval.eval(env);
                ra = rval.eval(env);
                if (la.getType() == Type.NIL && ra.getType() == Type.NIL) return Nil.NIL;
                return Num.TRUE;
            case AND:
                la = lval.eval(env);
                if (la.getType() == Type.NIL) return Nil.NIL;
                ra = rval.eval(env);
                if (ra.getType() == Type.NIL) return Nil.NIL;
                return Num.TRUE;
            case PAIR:
                return new Pair(lval.eval(env), rval.eval(env));
            case '.':
                if (lval.getType() == Type.NIL) la = env;
                else la = lval.eval(env);
                if (la.getType() == Type.ENV) {
                    return rval.eval((Env)la);
                } else {
                    throw new EvalException("can't access with . value [" + lval + "]");
                }
            default:
                throw new EvalException("unsupported operation: [" + op + "]");
        }
    }

    private String op() {
        switch(op) {
            case NEQ: return "<>";
            case LEQ: return "<=";
            case GEQ: return ">=";
            case OR:  return "||";
            case AND: return "&&";
            case PAIR: return "::";
            default:  return "" + op;
        }
    }

    @Override
    public String toTree() {
        return "(" + op + " " + lval.toTree() + " " + rval.toTree() + ")";
    }

    @Override
    public String toString() {
        return "" + lval + " " + rval + " " + op;
    }
}
