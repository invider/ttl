package io.ttl;

import org.junit.Test;

public class Level1 extends TestBase {

    @Test
    public void math() {
        eq("2+2", 4);
        val("print");
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
        val("2=2");
        val("2<>3");
        nil("2=3");
        nil("2<>2");
    }

    @Test
    public void testBasicExpr() {
        try {
            e("adsglhds + dsklgdjsl");
            assert false;
        } catch (Throwable t) {
            if (!(t instanceof  EvalException)) throw t;
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
        assert repl.exec("hw: 'hello world'").equals("'hello world'");
    }
}
