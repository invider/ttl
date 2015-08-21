package io.ttl;

import org.junit.Test;

/**
 * test frames, current/parent frame and nil literal
 */
public class Level7 extends LevelBase {

    public Level7() {}

    @Test
    public void frameTest() {
        e("f:[a:2, b:4, c:5, s:'this', t:'that']");
        eq("f.a", 2);
        eq("f.b", 4);
        eq("f.c", 5);
        eq("f.s", "this");
        eq("f.t", "that");
        nil("f.z");
        eq("f.a+f.b", 6);
        eq("f.a*f.c", 10);
        nil("f.c:nil");
        nil("f.c");
        eq(".f.a", 2);
        e("g:101");
        eq(".g", 101);
        eq(".f..g", 101);
        eq(".f..f.a", 2);
    }

    @Test
    public void frameIndexTest() {
        // TODO
    }

    @Test
    public void scopeTest() {
        eq("a:2, b:4, [a:20, b:40].(a+b)", 60);
    }
}
