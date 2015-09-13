package io.ttl.sys.math;

import io.ttl.val.Frame;
import io.ttl.val.Num;
import io.ttl.val.SysFun;
import io.ttl.val.Val;

public class Sin extends SysFun {

    @Override
    protected Val syscall(Frame frame) {
        return new Num(Math.sin(frame.val(0l).evalNum(frame)));
    }
}
