package me.tapeline.quailj.utils;

import me.tapeline.quailj.language.types.*;

import java.util.*;

public class ListUtils {

    public static ListType newListType(List<QType> content) {
        ListType l = new ListType();
        l.values = content;
        return l;
    }

    public static List<QType> reverse(List<QType> list) {
        List<QType> l = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            l.add(list.get(i));
        }
        return l;
    }

    public static List<QType> sub(List<QType> list, int begin) {
        return list.subList(begin, list.size() - 1);
    }

    public static List<QType> sub(List<QType> list, int begin, int stop) {
        return list.subList(begin, stop);
    }

    public static QType at(List<QType> list, int pos) {
        return list.get(pos);
    }

    public static List<QType> add(List<QType> values, QType value) {
        values.add(value);
        return values;
    }

    public static List<QType> addFront(List<QType> values, QType value) {
        List<QType> n = new ArrayList<>();
        n.add(value); n.addAll(values);
        return n;
    }

    public static List<QType> drop(List<QType> values, QType content) {
        if (content instanceof NumType)
            values.remove(NumUtils.round(((NumType) content).value));
        return values;
    }

    public static BoolType compare(List<QType> a, List<QType> b) {
        if (a.size() != b.size()) return new BoolType(false);
        for (int i = 0; i < a.size(); i++) {
            BoolType r = Utilities.compare(a.get(i), b.get(i));
            if (r == null) return null;
            if (!r.value) return new BoolType(false);
        }
        return new BoolType(true);
    }

    public static ListType concat(ListType a, ListType b) {
        a.values.addAll(b.values);
        return newListType(a.values);
    }

    public static List<QType> remove(List<QType> values, QType b) {
        values.remove(b);
        return values;
    }

    public static List<QType> removeAll(List<QType> values, List<QType> b) {
        values.removeAll(b);
        return values;
    }

    public static List<QType> mult(List<QType> values, double value) {
        long cc = NumUtils.round(value);
        List<QType> v = new ArrayList<>();
        for (long i = 0; i < cc; i++) v.addAll(values);
        return v;
    }

    public static List<QType> div(List<QType> s, double c) {
        int partLen = (int) Math.round(Math.floor(s.size() / c));
        List<QType> parts = new ArrayList<>();
        int i = 0;
        while (i < s.size()) {
            parts.add(new ListType(s.subList(i, i + partLen)));
            i += partLen;
        }
        if (i - partLen != s.size() - 1) {
            parts.add(new ListType(s.subList(i, s.size()-1)));
        }
        return parts;
    }

    public static List<QType> mod(List<QType> s, double c) {
        int partLen = (int) Math.round(Math.floor(s.size() / c));
        List<QType> parts = new ArrayList<>();
        int i = 0;
        while (i < s.size()) {
            parts.add(new ListType(s.subList(i, i + partLen)));
            i += partLen;
        }
        return parts;
    }
}