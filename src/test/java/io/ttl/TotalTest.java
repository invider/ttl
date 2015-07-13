package io.ttl;

import org.junit.Test;

public class TotalTest {

    Calc calc = new Calc();

    @Test
    public void testBasicExpr() {
        assert !calc.exec("2 + 2").equals("5");
        assert calc.exec("2 + 2").equals("4");
        assert calc.exec("2 + 2 * 3").equals("12");
    }
}
