package me.tapeline.quarkj.utils;

public class StringUtils {

    public static String reverse(String s) {
        String r = "";
        for (int i = s.length() - 1; i >= 0; i--) r += s.charAt(i);
        return r;
    }

    public static String sub(String s, int begin) {
        return s.substring(begin);
    }

    public static String sub(String s, int begin, int end) {
        return s.substring(begin, end);
    }

    public static String at(String s, int pos) {
        return s.charAt(pos) + "";
    }

    public static String upper(String s) {
        return s.toUpperCase();
    }

    public static String lower(String s) {
        return s.toLowerCase();
    }

    public static String capitalize(String s) {
        return (s.charAt(0) + "").toUpperCase() + s.substring(1).toLowerCase();
    }

    public static int len(String s) {
        return s.length();
    }
}
