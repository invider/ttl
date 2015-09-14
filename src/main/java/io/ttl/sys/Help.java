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
            StringBuilder res = new StringBuilder();
            String path = f.evalStr(frame);
            String tagPath = path + "!tag";
            String tagExtract = tagPath + "?" + tagPath + "||''";
            String tag = frame.getParent().eval(tagExtract).evalStr(frame);
            res.append(path);
            if (!tag.equals("")) {
                res.append(" - " + tag);
            }
            String manPath = path + "!man";
            String manExtract = manPath + "?" + manPath + "||''";
            String man = frame.getParent().eval(manExtract).evalStr(frame);
            if (!man.equals("")) {
                res.append("\n\nDESCRIPTION:\n\n").append(man).append('\n');
            }
            return new Str(res.toString());
        }
    }
}
