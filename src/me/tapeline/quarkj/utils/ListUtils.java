package me.tapeline.quarkj.utils;

import me.tapeline.quarkj.language.types.ListType;
import me.tapeline.quarkj.language.types.NumType;
import me.tapeline.quarkj.language.types.QType;

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

    public static List<QType> drop(List<QType> values, QType content) {
        if (content instanceof NumType)
            values.remove(NumUtils.round(((NumType) content).value));
        return values;
    }
}