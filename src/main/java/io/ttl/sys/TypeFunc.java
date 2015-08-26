package io.ttl.sys;

import io.ttl.val.Frame;
import io.ttl.val.Str;
import io.ttl.val.SysFun;
import io.ttl.val.Val;

public class TypeFunc extends SysFun {

    public TypeFunc() {
        this.name = "type";
    }

    @Override
    protected Val syscall(Frame frame) {
        Val val = frame.val(0l);
        return new Str(val.getType().toString());
    }
}
