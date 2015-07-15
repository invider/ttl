package io.ttl;

import org.junit.Test;

public class TotalTest {

    Calc calc = new Calc();

    @Test
    public void testBasicExpr() {
        try {
            calc.exec("adsglhds + dsklgdjsl");
            assert false;
        } catch (Throwable t) {
            assert t instanceof EvalException;
        }
        assert !calc.exec("2 + 2").equals("" + 5d);
        assert calc.exec("2 + 2").equals("" + 4d);
        assert calc.exec("2 + 2 * 3").equals("" + 12d);
    }
}
