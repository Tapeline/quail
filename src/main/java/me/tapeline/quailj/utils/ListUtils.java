package me.tapeline.quailj.utils;

import me.tapeline.quailj.types.*;

import java.util.*;

public class ListUtils {

    public static ListType newListType(List<QValue> content) {
        ListType l = new ListType();
        l.values = content;
        return l;
    }

    public static List<QValue> reverse(List<QValue> list) {
        List<QValue> l = new ArrayList<>();
        for (int i = list.size() - 1; i >= 0; i--) {
            l.add(list.get(i));
        }
        return l;
    }

    public static List<QValue> sub(List<QValue> list, int begin) {
        return list.subList(begin, list.size() - 1);
    }

    public static List<QValue> sub(List<QValue> list, int begin, int stop) {
        return list.subList(begin, stop);
    }

    public static QValue at(List<QValue> list, int pos) {
        return list.get(pos);
    }

    public static List<QValue> add(List<QValue> values, QValue value) {
        values.add(value);
        return values;
    }

    public static List<QValue> addFront(List<QValue> values, QValue value) {
        List<QValue> n = new ArrayList<>();
        n.add(value); n.addAll(values);
        return n;
    }

    public static List<QValue> drop(List<QValue> values, QValue content) {
        if (content.v instanceof NumType)
            values.remove(NumUtils.round(((NumType) content.v).value));
        return values;
    }

    public static BoolType compare(List<QValue> a, List<QValue> b) {
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

    public static List<QValue> remove(List<QValue> values, QValue b) {
        values.remove(b);
        return values;
    }

    public static List<QValue> removeAll(List<QValue> values, List<QValue> b) {
        values.removeAll(b);
        return values;
    }

    public static List<QValue> mult(List<QValue> values, double value) {
        long cc = NumUtils.round(value);
        List<QValue> v = new ArrayList<>();
        for (long i = 0; i < cc; i++) v.addAll(values);
        return v;
    }

    public static List<QValue> div(List<QValue> s, double c) {
        int partLen = (int) Math.round(Math.floor(s.size() / c));
        List<QValue> parts = new ArrayList<>();
        int i = 0;
        while (i < s.size()) {
            parts.add(new QValue(new ListType(s.subList(i, i + partLen))));
            i += partLen;
        }
        if (i - partLen != s.size() - 1) {
            parts.add(new QValue(new ListType(s.subList(i, s.size()-1))));
        }
        return parts;
    }

    public static List<QValue> mod(List<QValue> s, double c) {
        int partLen = (int) Math.round(Math.floor(s.size() / c));
        List<QValue> parts = new ArrayList<>();
        int i = 0;
        while (i < s.size()) {
            parts.add(new QValue(new ListType(s.subList(i, i + partLen))));
            i += partLen;
        }
        return parts;
    }
}