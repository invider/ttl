package io.ttl;

import org.junit.Test;

/**
 * global frame and variables
 */
public class Level3 extends LevelBase {

    @Test
    public void globalVariablesTest() {
        eq("a:2, a", 2);
        eq("b:4, b", 4);
        eq("c:5, c", 5);
        eq("a+a", 4);
        eq("a+b+c", 11);
        eq("b-a", 2);
        eq("a*a", 4);
        eq("c%a", 1);
        eq("c/a", 2.5);
        eq("c+10", 15);
        eq("c-3", 2);
        eq("a*2", 4);
        eq("b/2", 2);
        eq("c%2", 1);
        nil("z");
        eq("a:20, a", 20);
        eq("a+b", 24);
    }

    @Test
    public void namesTest() {
        success("someVariable:1");
        val("someVariable=1");
        success("really_long_variable_name:2");
        val("really_long_variable_name=2");
        success("$stored:'value'");
        val("$stored='value'");
    }

    @Test
    public void constantTest() {
        success("_c:7");
        eq("_c+1", 8);
        ex("_c:8");
    }
}
