package io.ttl;

import java.io.Reader;
import java.io.StringReader;

public class Calc extends Env implements Eval {

    @Override
    public String exec(String src) {
        try {
            Reader reader = new StringReader(src);
            Lex lex = new Lex(reader);
            Parser parser = new Parser(this, lex);
            return parser.parse();
        } catch (Throwable t) {
            throw new EvalException(t, src);
        }
    }
}
