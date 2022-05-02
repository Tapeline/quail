package me.tapeline.quailj.types;

public class QValue {

    public QType v;

    public QValue(QType q) {
        v = q;
    }

    public QValue(String s) {
        v = new StringType(s);
    }

    public QValue(boolean b) {
        v = new BoolType(b);
    }

    public QValue(double d) {
        v = new NumType(d);
    }

    public QValue() {
        v = new VoidType();
    }

    @Override
    public String toString() {
        return v.toString();
    }

    public static QValue nullSafe(QValue v) {
        return v == null? new QValue() : (v.v == null? new QValue(new VoidType()) : v);
    }

    public QValue nullSafeGet(String v) {
        if (this.v.table.containsKey(v))
            return this.v.table.get(v);
        return new QValue();
    }

    public static boolean isFunc(QValue... a) {
        for (QValue q : a) if (!(q.v instanceof FuncType)) return false;
        return true;
    }

    public static boolean isJava(QValue... a) {
        for (QValue q : a) if (!(q.v instanceof JavaType)) return false;
        return true;
    }

    public static boolean isBool(QValue... a) {
        for (QValue q : a) if (!(q.v instanceof BoolType)) return false;
        return true;
    }

    public static boolean isStr(QValue... a) {
        for (QValue q : a) if (!(q.v instanceof StringType)) return false;
        return true;
    }

    public static boolean isNum(QValue... a) {
        for (QValue q : a) if (!(q.v instanceof NumType)) return false;
        return true;
    }

    public static boolean isList(QValue... a) {
        for (QValue q : a) if (!(q.v instanceof ListType)) return false;
        return true;
    }

    public static boolean isCont(QValue... a) {
        for (QValue q : a) if (!(q.v instanceof ContainerType)) return false;
        return true;
    }

    public static boolean isNull(QValue... a) {
        for (QValue q : a) if (!(q.v instanceof VoidType) && q.v != null) return false;
        return true;
    }

    public QValue copy() {
        return new QValue(v.copy());
    }

}
