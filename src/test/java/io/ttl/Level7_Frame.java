package io.ttl;

import org.junit.Test;

/**
 * test frames, current/parent frame and nil literal
 */
public class Level7_Frame extends LevelBase {

    public Level7_Frame() {}

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
        success("f.c:nil");
        nil("f.c");
        eq(".f.a", 2);
        e("g:101");
        eq(".g", 101);
        eq(".f..g", 101);
        eq(".f..f.a", 2);
    }

    @Test
    public void frameIndexTest() {
        success("f:[100,101,102,103,104]");
        eq("f.@0", 100);
        eq("f.@1", 101);
        eq("f.@2", 102);
        eq("f.@3", 103);
        eq("f.@4", 104);
    }

    @Test
    public void scopeTest() {
        eq("a:2, b:4, [a:20, b:40].(a+b)", 60);
    }
}
