package io.ttl.val;

import io.ttl.Env;
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

    @Override
    public Val eval(Env env) {
        switch (op) {
            case "+":
                Val lres = lval.eval(env);
                if (lres.getType() == Type.string) {
                    return new Str(lres.evalStr(env) + rval.evalStr(env));
                } else if (lres.getType() == Type.num) {
                    return new Num(lres.evalNum(env) + rval.evalNum(env));
                } else if (lres.getType() == Type.nil) {
                    return Nil.NIL;
                } else {
                   throw new EvalException("wrong operands: "
                        + lval.toString() + " + " + rval.toString());
                }
            case "-":
                return new Num(lval.evalNum(env) - rval.evalNum(env));
            case "*":
                return new Num(lval.evalNum(env) * rval.evalNum(env));
            case "/":
                return new Num(lval.evalNum(env) / rval.evalNum(env));
            case "%":
                return new Num(lval.evalNum(env) % rval.evalNum(env));
            case ":":
                Id id = (Id)lval.expect(Type.id);
                Val val = rval.eval(env);
                env.set(id.name, val);
                return val;
            default:
                throw new EvalException("unknown operator: " + op);
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
        return "" + lval + " " + rval + " " + op;
    }

    @Override
    public String toTree() {
        return "(" + op
                + " " + lval.toTree()
                + " " + rval.toTree() + ")";
    }
}
