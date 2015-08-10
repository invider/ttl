package io.ttl.sys;

import io.ttl.Env;
import io.ttl.val.Nil;
import io.ttl.val.SysFun;
import io.ttl.val.Val;

public class ExitCall extends SysFun {

    public ExitCall() {
        this.name = "exit";
    }

    @Override
    protected Val syscall(Env env) {
        System.exit(0);
        return Nil.NIL;
    }
}
