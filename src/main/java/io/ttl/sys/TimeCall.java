package io.ttl.sys;

import io.ttl.Env;
import io.ttl.val.Str;
import io.ttl.val.SysFun;
import io.ttl.val.Val;

import java.util.Date;

public class TimeCall extends SysFun {

    public TimeCall() {
        this.name = "time";
    }

    @Override
    protected Val syscall(Env env) {
        return new Str((new Date()).toString());
    }

}
