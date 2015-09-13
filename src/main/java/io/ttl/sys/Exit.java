package io.ttl.sys;

import io.ttl.val.Frame;
import io.ttl.val.Nil;
import io.ttl.val.SysFun;
import io.ttl.val.Val;

public class Exit extends SysFun {

    protected Val syscall(Frame frame) {
        System.exit(0);
        return Nil.NIL;
    }
}
