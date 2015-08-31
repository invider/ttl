package io.ttl.val;

import io.ttl.EvalException;

import java.security.KeyStore;
import java.util.Map;

public class Loop extends While implements Val {

    public Loop(Val condVal, Val bodyVal) {
        super(condVal, bodyVal);
    }

    @Override
    public Val expect(Type t) {
        if (t != Type.op) {
            throw new EvalException(
                    "" + t + " was expected, but *~ found");
        }
        return this;
    }

    @Override
    public Val eval(Frame frame) {
        Val res = Nil.NIL;
        Val iter = condVal.eval(frame);
        Frame scope = new Frame(frame);

        if (iter.getType() == Val.Type.num) {
            double it = 0;
            for (int i = iter.evalNum(frame).intValue(); i > 0; i--) {
                scope.set("it", new Num(it++));
                res = bodyVal.eval(scope);
            }
        } else if (iter.getType() == Type.list) {
            res = applyElement(scope, (List) iter);
        } else if (iter.getType() == Type.frame) {
            for(Map.Entry<Long, Val> e: ((Frame)iter).numMap.entrySet()) {
                scope.set("name", new Num(e.getKey()));
                scope.set("it", e.getValue());
                res = bodyVal.eval(scope);
            }
            for (Map.Entry<String, Val> e: ((Frame)iter).map.entrySet()) {
                scope.set("name", new Str(e.getKey()));
                scope.set("it", e.getValue());
                res = bodyVal.eval(scope);
            }
        } else {
            throw new EvalException("*~ can't iterate over [" + iter + "]");
        }
        return res;
    }

    private Val applyElement(Frame scope, List list) {
        Val it = list.head().eval(scope);
        scope.set("it", it);
        bodyVal.eval(scope);
        it = list.tail().eval(scope);
        if (it.getType() == Type.list) {
            return applyElement(scope, (List)it);
        } else {
            scope.set("it", it);
            return bodyVal.eval(scope);
        }
    }

    @Override
    public String toString() {
        return "[" + condVal + "]*~ [" + bodyVal + "]";
    }

    @Override
    public String toTree() {
        return "(*~ "
                + " " + condVal.toTree()
                + " " + bodyVal.toTree() + ")";
    }
}
