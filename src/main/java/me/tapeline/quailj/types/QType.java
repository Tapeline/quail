package me.tapeline.quailj.types;

import java.util.HashMap;

public class QType {

    public HashMap<String, QType> table;

    public static boolean isFunc(QType... a) {
        for (QType q : a) if (!(q instanceof FuncType)) return false;
        return true;
    }

    public static boolean isJava(QType... a) {
        for (QType q : a) if (!(q instanceof JavaType)) return false;
        return true;
    }

    public QType copy() {
        QType v = new QType();
        if (v.table != null && table != null)
        v.table.putAll(this.table);
        return v;
    }

    public static QType nullSafe(QType v) {
        return v == null? new VoidType() : v;
    }

    public static boolean isNum(QType a) {
        return a instanceof NumType;
    }

    public static boolean isNum(QType a, QType b) {
        return a instanceof NumType && b instanceof NumType;
    }

    public static boolean isNum(QType a, QType b, QType c) {
        return a instanceof NumType && b instanceof NumType && c instanceof NumType;
    }

    public static boolean isBool(QType a) {
        return a instanceof BoolType;
    }

    public static boolean isBool(QType a, QType b) {
        return a instanceof BoolType && b instanceof BoolType;
    }

    public static boolean isStr(QType a) {
        return a instanceof StringType;
    }

    public static boolean isStr(QType a, QType b) {
        return a instanceof StringType && b instanceof StringType;
    }

    public static boolean isStr(QType a, QType b, QType c) {
        return a instanceof StringType && b instanceof StringType && c instanceof StringType;
    }

    public static boolean isList(QType a) {
        return a instanceof ListType;
    }

    public static boolean isList(QType a, QType b) {
        return a instanceof ListType && b instanceof ListType;
    }

    public static boolean isCont(QType a) {
        return a instanceof ContainerType;
    }

    public static boolean isCont(QType a, QType b) {
        return a instanceof ContainerType && b instanceof ContainerType;
    }

}
