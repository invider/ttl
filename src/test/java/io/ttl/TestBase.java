package io.ttl;

public class TestBase {

    protected REPL repl = new REPL();

    protected double e(String str) {
        String res = repl.exec(str).trim();
        return Double.parseDouble(res);
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

    protected void val(String src) {
        String res = repl.exec(src).trim();
        assert !res.equals("<NIL>");
    }

    protected void nil(String src) {
        String res = repl.exec(src).trim();
        assert res.equals("<NIL>");
    }

}
