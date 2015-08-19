package io.ttl.val;

import io.ttl.EvalException;

public class Fun implements Val {

    private Val id, params;

    public Fun(Val id, Val params) {
        this.id = id;
        this.params = params;
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.fun) {
            throw new EvalException(
                    "" + t + " was expected, but a function [" + id + "] found");
        }
        return this;
    }

    @Override
    public Type getType() {
        return Val.Type.fun;
    }

    @Override
    public boolean isAtom() {
        return false;
    }

    @Override
    public Val eval(Frame frame) {
        Val call = id.eval(frame);
        if (call.getType() == Type.sys) {
            Frame funFrame = new Frame(frame);
            funFrame.set(params.eval(funFrame));
            return call.eval(funFrame);
        } else {
            if (call.getType() != Type.string) {
                throw new EvalException("function body was expected, but ["
                        + call + "] is found");
            }
            String src = call.evalStr(frame);
            Frame funFrame = new Frame(frame);
            if (params.getType() == Type.op && ((Op)params).matchOperator(":")) {
                params.eval(funFrame); // name association side effect
            } else {
                funFrame.set(params.eval(funFrame)); // index association
            }
            return funFrame.eval(src);
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
    public String toString() {
        return "" + id + "(" + params + ")";
    }

    @Override
    public String toTree() {
        return "(" + id.toTree() + " " + params.toTree() + ")";
    }
}
