package io.ttl.sys;

import io.ttl.EvalException;
import io.ttl.val.Frame;
import io.ttl.val.SysFun;
import io.ttl.val.Val;

public class True extends SysFun {

    @Override
    protected Val syscall(Frame frame) {
        Val sample = frame.val(0l);
        if (sample.getType() == Type.nil) {
            throw new EvalException("value was asserted, but nil was found");
        }
        return Val.TRUE;
    }
}
