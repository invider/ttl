package io.ttl;

import org.junit.Test;

/**
 * test for simple math expressions: + - * / %
 */
public class Level1 extends LevelBase {

    @Test
    public void spacedExpressionTest() {
        eq("2 + 2", 4);
        eq("2 + 2 + 2", 6);
        eq("3 - 1", 2);
        eq("5 - 1 - 2", 2);
        eq("2 - 4", -2);
        eq("2 + 2 - 2", 2);
        eq("5 - 2 + 1", 4);
        eq("1.5 + 2.5", 4);
        eq("6.1 - 0.1 + 0.2", 6.2);
        eq("2 * 2", 4);
        eq("2 * 2 * 2", 8);
        eq("4 / 2", 2);
        eq("8 / 2 / 2", 2);
        eq("1 / 2", 0.5);
        eq("8 * 2 / 2", 8);
        eq("8 / 2 * 4", 16);
        eq("4 % 2", 0);
        eq("5 % 2", 1);
        eq("5 * 3 % 2", 1);
        eq("5 % 2 * 4", 4);
        eq("2.5 % 2", 0.5);
    }

    @Test
    public void packedExpressionTest() {
        eq("2+2", 4);
        eq("2+2+2", 6);
        eq("3-1", 2);
        eq("5-1-2", 2);
        eq("2-4", -2);
        eq("2+2-2", 2);
        eq("5-2+1", 4);
        eq("1.5+2.5", 4);
        eq("6.1-0.1+0.2", 6.2);
        eq("2*2", 4);
        eq("2*2*2", 8);
        eq("4/2", 2);
        eq("8/2/2", 2);
        eq("1/2", 0.5);
        eq("8*2/2", 8);
        eq("8/2*4", 16);
        eq("4%2", 0);
        eq("5%2", 1);
        eq("5*3%2", 1);
        eq("5%2*4", 4);
        eq("2.5%2", 0.5);
    }


    @Test
    public void testBasicExpr() {
        ex("notavariable + anothermissingvariable");
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
