package io.ttl.sys;

import io.ttl.val.Frame;
import io.ttl.val.Nil;
import io.ttl.val.SysFun;
import io.ttl.val.Val;

import java.util.Map;

public class Print extends SysFun{

    public Print() {
        this.name = "print";
    }

    @Override
    protected Val syscall(Frame frame) {
        Map<Long, Val> map = frame.getNumMap();
        for (Long i: map.keySet()) {
            Val v = map.get(i);
            System.out.println(v.evalStr(frame));
        }
        return Nil.NIL;
    }
}
