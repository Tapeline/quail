package me.tapeline.quailj.utils;

import me.tapeline.quailj.language.types.QType;
import me.tapeline.quailj.language.types.StringType;

import java.util.ArrayList;
import java.util.List;

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

    public static String mult(String s, double c) {
        long cc = NumUtils.round(c);
        String ss = "";
        for (long i = 0; i < cc; i++) ss += s;
        return ss;
    }

    public static List<QType> div(String s, double c) {
        int partLen = (int) Math.round(Math.floor(s.length() / c));
        List<QType> parts = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            parts.add(new StringType(s.substring(i, i + partLen)));
            i += partLen;
        }
        if (i - partLen != s.length() - 1) {
            parts.add(new StringType(s.substring(i)));
        }
        return parts;
    }

    public static List<QType> mod(String s, double c) {
        int partLen = (int) Math.round(Math.floor(s.length() / c));
        List<QType> parts = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            parts.add(new StringType(s.substring(i, i + partLen)));
            i += partLen;
        }
        return parts;
    }

    public static List<QType> split(String s, String d) {
        String[] l = s.split(d);
        List<QType> ll = new ArrayList<>();
        for (String s1 : l)
            ll.add(new StringType(s1));
        return ll;
    }
}
