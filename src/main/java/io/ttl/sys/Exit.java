package io.ttl.sys;

import io.ttl.val.Env;
import io.ttl.val.Fun;
import io.ttl.Total;
import io.ttl.val.Nil;
import io.ttl.val.Val;

public class Exit extends Fun {

    @Override
    public Val eval(Env env) {
        Total.loop = false;
        return Nil.NIL;
    }
}
