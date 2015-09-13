package io.ttl.sys;

import io.ttl.Util;
import io.ttl.val.Frame;
import io.ttl.val.Str;
import io.ttl.val.SysFun;
import io.ttl.val.Val;

public class Help extends SysFun {

    @Override
    protected Val syscall(Frame frame) {
        Val f = frame.val(0l);
        if (f.getType() == Type.nil) {
            return frame.getParent().val("man.intro");
        } else {
            StringBuilder man = new StringBuilder();
            String path = f.evalStr(frame);
            String tagPath = path + "!tag";
            String tagExtract = tagPath + "?" + tagPath + "||''";
            String tag = frame.getParent().eval(tagExtract).evalStr(frame);
            man.append(path);
            if (!tag.equals("")) {
                man.append(" - " + tag);
            }
            return new Str(man.toString());
        }
    }
}
