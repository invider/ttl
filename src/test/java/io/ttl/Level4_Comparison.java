package io.ttl;

import org.junit.Test;

/**
 * test comparison operators
 */
public class Level4_Comparison extends LevelBase {

    public Level4_Comparison() {
        super();
        e("a:2");
        e("b:4");
        e("c:5");
        e("d:2");
        e("e:1");
        e("s:'this'");
        e("t:'that'");
    }

    @Test
    public void equalTest() {
        val("2=2");
        nil("2=4");
        val("a=2");
        nil("a=4");
        val("a=d");
        nil("a=c");
        val("'this'='this'");
        nil("'this'='that'");
        val("s=s");
        val("s='this'");
        nil("s=t");
        nil("s='that'");
    }

    @Test
    public void notEqualTest() {
        nil("2<>2");
        val("2<>4");
        nil("a<>2");
        val("a<>4");
        nil("a<>d");
        val("a<>c");
        val("'this'='this'");
        nil("'this'='that'");
        nil("s<>s");
        nil("s<>'this'");
        val("s<>t");
        val("s<>'that'");
    }

    @Test
    public void lessTest() {
        val("2<4");
        nil("2<2");
        val("a<4");
        nil("a<2");
        val("a<c");
        nil("a<d");
        val("a<=d");
        val("a<=c");
        nil("a<=1");
        nil("a<=e");
        val("'aaa'<'bbb'");
        val("'aaa'<='aaa'");
        nil("'bbb'<'aaa'");
        nil("'bbb'<='aaa'");
    }

    @Test
    public void greaterTest() {
        val("4>2");
        nil("2>2");
        val("a>1");
        nil("a>2");
        val("a>e");
        nil("a>d");
        val("a>=d");
        val("a>=2");
        val("a>=1");
        nil("a>=c");
        nil("a>=4");
        val("a>=1");
        nil("'aaa'>'bbb'");
        val("'aaa'>='aaa'");
        val("'bbb'>'aaa'");
        val("'bbb'>='aaa'");
    }

    @Test
    public void notTest() {
        nil("!2");
        nil("!'this'");
        nil("!a");
        nil("!s");
        val("!nil");
        val("!z");
    }
}
