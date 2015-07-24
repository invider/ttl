package io.ttl.sys;

import io.ttl.Pile;
import io.ttl.val.Fun;
import io.ttl.Total;

public class Exit extends Fun {

    public Exit(Pile pile) {
        super(pile);
    }

    @Override
    public String eval() {
        Total.loop = false;
        return "";
    }
}
