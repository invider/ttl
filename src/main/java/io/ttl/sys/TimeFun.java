package io.ttl.sys;

import io.ttl.val.Frame;
import io.ttl.val.Str;
import io.ttl.val.SysFun;
import io.ttl.val.Val;

import java.util.Date;

public class TimeFun extends SysFun {

    public TimeFun() {
        this.name = "time";
    }

    @Override
    protected Val syscall(Frame frame) {
        return new Str((new Date()).toString());
    }

}