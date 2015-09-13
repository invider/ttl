package io.ttl.sys;

import io.ttl.EvalException;
import io.ttl.Parser;
import io.ttl.Util;
import io.ttl.val.Frame;
import io.ttl.val.Num;
import io.ttl.val.SysFun;
import io.ttl.val.Val;

import java.util.Map;

public class Test extends SysFun {
    @Override
    protected Val syscall(Frame frame) {
        Util.out("self-diagnostics...");
        double score = testFrame(frame.getParent().getParent());
        return new Num(score);
    }

    private double testFrame(Frame frame) {
        double score = 0;
        for (Map.Entry<String, Val> e: frame.getNameMap().entrySet()) {
            String name = e.getKey();
            if (name.endsWith("!test")) {
                name = e.getKey().substring(0, name.length() - 5);
                String body = e.getValue().evalStr(frame);
                System.out.print("testing " + name + "... ");
                try {
                    Val res = frame.eval(body);
                    if (res.getType() == Val.Type.nil) {
                        throw new EvalException("test returned nil value for [" + name + "]");
                    }
                    score += res.evalNum(frame);
                    System.out.print("OK");
                } catch (Throwable t) {
                    System.out.println("Failed");
                }
                // wait
                try {
                    Thread.currentThread().sleep(250);
                } catch (InterruptedException ie) {}
                System.out.print("\r");
                System.out.print("                                        ");
                System.out.print("\r");
            }
        }
        return score;
    }

}
