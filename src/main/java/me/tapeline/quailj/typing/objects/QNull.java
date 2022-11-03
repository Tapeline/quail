package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.typing.utils.VariableTable;

public class QNull extends QObject {

    public static VariableTable defaults = new VariableTable();

    public QNull() {
        table.putAll(defaults);
        setObjectMetadata("Null");

    }

    public String toString() {
        return "null";
    }

}
