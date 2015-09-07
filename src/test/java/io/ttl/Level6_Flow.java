package io.ttl;

import org.junit.Test;

/**
 * test flow: expr, expr, expr...
 */
public class Level6_Flow extends LevelBase {

    public Level6_Flow() {}

    @Test
    public void flowTest() {
        eq("a:2, b:4, c:a*b, c", 8);
    }
}
