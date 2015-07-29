package io.ttl;

import io.ttl.val.Num;
import io.ttl.val.Str;
import io.ttl.val.Val;

import java.io.Reader;
import java.io.StringReader;

public class Calc extends Env implements Eval {

    public Calc() {
        this.set("version", new Str("Version 0.2"));
        this.set("_pi", new Num(3.14d));
    }

    @Override
    public String exec(String src) {
        try {
            Reader reader = new StringReader(src);
            Lex lex = new Lex(reader);
            Parser parser = new Parser(this, lex);
            Val val = parser.parse();
            System.out.println("< " + val);
            System.out.println("& " + val.toTree());
            return val.eval(this).toString();
        } catch (Throwable t) {
            throw new EvalException(t, src);
        }
    }
}
