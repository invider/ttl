package io.ttl;

import org.junit.Test;

/**
 * test conditional operator ? ... !! ... and logical && ||
 */
public class Level5 extends LevelBase {

    public Level5() {}

    @Test
    public void conditionTest() {
        eq("2=2?'yes'", "yes");
        nil("2<>2?'yes'");
        eq("2<>2?'yes'!!'no'", "no");
    }

    @Test
    public void andTest() {
        eq("2=2&&4=4?'yes'!!'no'", "yes");
        eq("2=2&&4=3?'yes'!!'no'", "no");
        eq("2=3&&4=4?'yes'!!'no'", "no");
        eq("2=3&&4=3?'yes'!!'no'", "no");
    }

    @Test
    public void orTest() {
        eq("2=2||4=4?'yes'!!'no'", "yes");
        eq("2=2&&4=3?'yes'!!'no'", "no");
        eq("2=3&&4=4?'yes'!!'no'", "no");
        eq("2=3&&4=3?'yes'!!'no'", "no");
    }
}
