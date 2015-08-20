package io.ttl;

public class LevelBase {

    protected REPL repl = new REPL();

    protected double e(String str) {
        String res = repl.exec(str).trim();
        try {
            return Double.parseDouble(res);
        } catch (NumberFormatException e) {
            throw new EvalException("number was expected", e);
        }
    }

    protected void eq(String src, double out) {
        assert e(src) == out;
    }

    protected void eq(String src, String out) {
        String res = repl.exec(src).trim();
        assert res.equals(out);
    }

    protected void neq(String src, double out) {
        assert e(src) != out;
    }

    protected void neq(String src, String out) {
        String res = repl.exec(src).trim();
        assert !res.equals(out);
    }

    /**
     * expect value (true condition)
     */
    protected void val(String src) {
        String res = repl.exec(src).trim();
        assert !res.equals("<NIL>");
    }

    /**
     * expect nil
     */
    protected void nil(String src) {
        String res = repl.exec(src).trim();
        assert res.equals("<NIL>");
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
