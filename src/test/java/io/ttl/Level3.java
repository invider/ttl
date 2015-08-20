package io.ttl;

import org.junit.Test;

/**
 * global frame and variables
 */
public class Level3 extends LevelBase {

    @Test
    public void globalVariables() {
        eq("a:2", 2);
        eq("b:4", 4);
        eq("c:5", 5);
        eq("a+a", 4);
        eq("a+b+c", 11);
        eq("a*a", 4);
        eq("c%a", 1);
        eq("c/a", 2.5);
    }
}
