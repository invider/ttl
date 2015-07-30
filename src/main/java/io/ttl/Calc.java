package io.ttl;

import io.ttl.val.*;

import java.io.Reader;
import java.io.StringReader;

public class Calc extends Env implements Eval {

    @Override
    public String exec(String src) {
        return "" + apply(this, src);
    }

    public Val apply(Env env, String src) {
        Val res = Nil.NIL;
        src = src.trim();
        if (src.length() == 0) return res;
        Lex lex = new Lex(new StringReader(src));

        try {
            res = (new Parser(lex)).parse();
            System.out.println("# " + res);
            System.out.println("% " + res.toTree());
        } catch (EvalException e) {
            e.setSrc(lex.getCurrentLine());
            throw e;
        } catch (Throwable t) {
            throw new EvalException(t, lex.getCurrentLine());
        }
        try {
            return res.eval(env);
        } catch (EvalException e) {
            throw e;
        } catch (Throwable t) {
            throw new EvalException(t, "eval system error: " + t.getMessage());
        }
    }
}
