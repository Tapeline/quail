package me.tapeline.quailj.utils;

import me.tapeline.quailj.types.*;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {

    public static String reverse(String s) {
        StringBuilder r = new StringBuilder();
        for (int i = s.length() - 1; i >= 0; i--) r.append(s.charAt(i));
        return r.toString();
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
        StringBuilder ss = new StringBuilder();
        for (long i = 0; i < cc; i++) ss.append(s);
        return ss.toString();
    }

    public static List<QValue> div(String s, double c) {
        int partLen = (int) Math.round(Math.floor(s.length() / c));
        List<QValue> parts = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            parts.add(new QValue(s.substring(i, i + partLen)));
            i += partLen;
        }
        if (i - partLen != s.length() - 1) {
            parts.add(new QValue(s.substring(i)));
        }
        return parts;
    }

    public static List<QValue> mod(String s, double c) {
        int partLen = (int) Math.round(Math.floor(s.length() / c));
        List<QValue> parts = new ArrayList<>();
        int i = 0;
        while (i < s.length()) {
            parts.add(new QValue(s.substring(i, i + partLen)));
            i += partLen;
        }
        return parts;
    }

    public static List<QValue> split(String s, String d) {
        String[] l = s.split(d);
        List<QValue> ll = new ArrayList<>();
        for (String s1 : l)
            ll.add(new QValue(s1));
        return ll;
    }
}
