package io.ttl;

import io.ttl.sys.ExitCall;
import io.ttl.sys.TimeCall;
import io.ttl.val.*;

import java.io.Reader;
import java.io.StringReader;

public class Calc extends Scope {

    public Calc() {
        this.set("version", new Str("Version 0.2"));
        this.set("_pi", new Num(3.14d));
        defun(new ExitCall());
        defun(new TimeCall());
    }

    private void defun(SysFun sysfun) {
        this.set(sysfun.getName(), sysfun);
    }

    @Override
    public Val eval(String src, Scope scope) {
        try {
            Reader reader = new StringReader(src);
            Lex lex = new Lex(reader);
            Parser parser = new Parser(this, lex);
            Val val = parser.parse();
            //System.out.println("< " + val);
            //System.out.println("& " + val.toTree());
            return val.eval(scope);
        } catch (Throwable t) {
            throw new EvalException(t, src);
        }
    }
}
