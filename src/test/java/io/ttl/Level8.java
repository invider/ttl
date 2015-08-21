package io.ttl;

import org.junit.Test;

/**
 * test function calls
 */
public class Level8 extends LevelBase {

    @Test
    public void functionCallTest()  {
        eq("'2'()", 2);
        eq("f:'2', f()", 2);
        eq("f:'a+1', f(a:2)", 3);
        eq("f:'@0', f(7)", 7);
    }
}
