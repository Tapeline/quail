package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.utils.VariableTable;

public class QBool extends QObject {

    public static VariableTable defaults = new VariableTable();

    public boolean value;

    public QBool(boolean value) {
        table.putAll(defaults);
    }

    public QObject not(Runtime runtime) {
        return Val(!value);
    }

    public QObject minus(Runtime runtime) {
        return Val(!value);
    }

    public QObject and(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isBool())
            return Val(value && ((QBool) other).value);
        return super.and(runtime, other);
    }

    public QObject or(Runtime runtime, QObject other) throws RuntimeStriker {
        if (other.isBool())
            return Val(value || ((QBool) other).value);
        return super.and(runtime, other);
    }

}
