package io.ttl;

import io.ttl.val.Nil;
import io.ttl.val.Success;

public class LevelBase {

    protected REPL repl = new REPL(false);

    /**
     * just execute
     */
    protected String e(String src) {
        return repl.exec(src);
    }

    /**
     * execute with double result expected
     * @return double value
     */
    protected double ed(String str) {
        String res = repl.exec(str).trim();
        try {
            return Double.parseDouble(res);
        } catch (NumberFormatException e) {
            throw new EvalException("number was expected", e);
        }
    }

    /**
     * execute and compare result with double value
     */
    protected void eq(String src, double out) {
        assert ed(src) == out;
    }

    /**
     * execute and compare result with str value
     */
    protected void eq(String src, String out) {
        String str = repl.eval(src).evalStr(repl);
        if (!str.equals(out)) throw new EvalException("str '"
            + out + "' was expected, but [" + str + "] found");
    }

    /**
     * expect double result is not equal to passed double value
     */
    protected void neq(String src, double out) {
        assert ed(src) != out;
    }

    /**
     * expect str result is not equal to passed str value
     */
    protected void neq(String src, String out) {
        String res = repl.exec(src).trim();
        assert !res.equals(out);
    }

    /**
     * expect value (true condition)
     */
    protected void val(String src) {
        assert !repl.eval(src).equals(Nil.NIL);
    }

    /**
     * expect nil
     */
    protected void nil(String src) {
        assert repl.eval(src).equals(Nil.NIL);
    }

    /**
     * expect success
     */
    protected void success(String src) {
        assert repl.eval(src).equals(Success.SUCCESS);
    }

    /**
     * expect exception
     */
    protected void ex(String src) {
        try {
            String res = repl.exec(src);
            throw new EvalException("exception was expected for: " + src);
        } catch (EvalException e) {
            return;
        } catch (Throwable t){
            throw new EvalException("eval exception was expected, but "
                + t.getClass().getSimpleName() + " found", t);
        }
    }
}
