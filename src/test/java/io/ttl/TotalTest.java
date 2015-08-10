package io.ttl;

import org.junit.Test;

public class TotalTest {

    Env env = new Calc();

    private double e(String str) {
        String res = env.exec(str).trim();
        return Double.parseDouble(res);
    }

    @Test
    public void testBasicExpr() {
        /*
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
        assert eval.exec("hw: 'hello world'").equals("hello world");
        */
    }
}
