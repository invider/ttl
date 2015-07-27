package io.ttl;

import org.junit.Test;

public class TotalTest {

    Eval eval = new Calc();

    private double e(String str) {
        String res = eval.exec(str).trim();
        return Double.parseDouble(res);
    }

    private void eq(String src, double out) {
        assert e(src) == out;
    }

    private void eq(String src, String out) {
        String res = eval.exec(src).trim();
        assert res.equals(out);
    }

    private void neq(String src, double out) {
        assert e(src) != out;
    }

    private void neq(String src, String out) {
        String res = eval.exec(src).trim();
        assert !res.equals(out);
    }

    private void te(String src) {
        String res = eval.exec(src).trim();
        assert !res.equals("<NIL>");
    }

    private void nil(String src) {
        String res = eval.exec(src).trim();
        assert res.equals("<NIL>");
    }

    @Test
    public void testMathExpr() {
        eq("2+2", 4);
        eq("2-5", -3d);
        eq("4*4", 16);
        eq("16/2", 8);
        eq("10%3", 1);
        eq("2+2*3", 8);
        eq("2+2*(1+1)", 6);
    }

    @Test
    public void testCompExpr() {
        te("2=2");
        te("2<>3");
        nil("2=3");
        nil("2<>2");
    }

    @Test
    public void testBasicExpr() {
        try {
            e("adsglhds + dsklgdjsl");
            assert false;
        } catch (Throwable t) {
            assert t instanceof EvalException;
        }
        assert !(e("2 + 2") == 5d);
        assert e("2 + 2") == 4d;
        assert e("2 + 2 * 3") == 8d;
        // make things more complicated
        assert e("2+2") == 4d;
        assert e("2+2*4") == 10d;
        assert e("2*(3+4) + 10") == 24d;
        assert e("1+(1*1+(1+1)*1)") == 4d;
        assert e("pi:3.14, 2*pi") == 6.28d;
        assert eval.exec("hw: 'hello world'").equals("'hello world'");
    }
}
