package io.ttl.sys.io;

import io.ttl.val.Frame;
import io.ttl.val.Nil;
import io.ttl.val.SysFun;
import io.ttl.val.Val;

import java.util.Map;

public class Prin extends SysFun {

    protected Val syscall(Frame frame) {
        Map<Long, Val> map = frame.getNumMap();
        for (Long i: map.keySet()) {
            Val v = map.get(i);
            System.out.print(v.evalStr(frame));
        }
        return Nil.NIL;
    }
}
