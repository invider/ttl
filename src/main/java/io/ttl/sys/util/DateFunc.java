package io.ttl.sys.util;

import io.ttl.val.Frame;
import io.ttl.val.Str;
import io.ttl.val.SysFun;
import io.ttl.val.Val;

import java.util.Date;

public class DateFunc extends SysFun {

    public DateFunc() {
        super("date");
    }

    @Override
    protected Val syscall(Frame frame) {
        return new Str((new java.util.Date()).toString());
    }
}
