package io.ttl;

public class Util {

    public static void out(String msg) {
        System.out.println(msg);
    }

    public static void log(String msg) {
        System.out.println("> " + msg);
    }

    public static void res(String msg) {
        System.out.println("= " + msg);
    }

    public static void debug(String msg) {
        System.out.println("# " + msg);
    }

    public static void warn(String msg) {
        System.out.println("% " + msg);
    }

    public static void error(String msg) {
        System.out.println("! " + msg);
    }
}
