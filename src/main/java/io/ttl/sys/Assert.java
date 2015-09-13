package io.ttl.sys;

import io.ttl.EvalException;
import io.ttl.val.Frame;
import io.ttl.val.SysFun;
import io.ttl.val.Val;

public class Assert extends SysFun {

    @Override
    protected Val syscall(Frame frame) {
        Val sample = frame.val(0l);
        Val expected = frame.val(1l);
        if (sample.getType() == expected.getType()
                && sample.eq(expected, frame)) {
            return Val.TRUE;
        }
        throw new EvalException("assert failed: " + sample + " <> " + expected);
    }
}
