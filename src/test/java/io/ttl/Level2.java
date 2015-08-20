package io.ttl;

import org.junit.Test;

/**
 * priorities for + - * / % and () operator
 */
public class Level2 extends LevelBase {

    @Test
    public void prioritiesTest() {
        eq("2+2*2", 6);
        eq("2*2+2", 6);
        eq("2+2/2", 3);
        eq("2/2+2", 3);
        eq("2+5%2", 3);
        eq("5%2+2", 3);
        eq("2*2+4*4", 20);
    }

    @Test
    public void parenthesesTest() {
        eq("4*(2+2)", 16);
        eq("4/(2+2)", 1);
        eq("5%(1+1)", 1);
    }
}
