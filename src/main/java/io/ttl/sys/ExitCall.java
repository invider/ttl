package io.ttl.sys;

import io.ttl.val.Scope;
import io.ttl.val.Nil;
import io.ttl.val.SysFun;
import io.ttl.val.Val;

public class ExitCall extends SysFun {

    public ExitCall() {
        this.name = "exit";
    }

    @Override
    protected Val syscall(Scope scope) {
        System.exit(0);
        return Nil.NIL;
    }
}
