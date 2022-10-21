package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.typing.utils.VariableTable;

public class QNumber extends QObject {

    public static VariableTable defaults = new VariableTable();

    public double value;

    public QNumber(double value) {
        table.putAll(defaults);

    }

}
