package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.typing.utils.VariableTable;

import java.util.List;

public class QList extends QObject {

    public static VariableTable defaults = new VariableTable();

    public List<QObject> values;

    public QList(List<QObject> values) {
        table.putAll(defaults);
    }

}
