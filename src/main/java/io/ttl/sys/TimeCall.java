package io.ttl.sys;

import io.ttl.val.Scope;
import io.ttl.val.Str;
import io.ttl.val.SysFun;
import io.ttl.val.Val;

import java.util.Date;

public class TimeCall extends SysFun {

    public TimeCall() {
        this.name = "time";
    }

    @Override
    protected Val syscall(Scope scope) {
        return new Str((new Date()).toString());
    }

}
