package me.tapeline.quailj.typing.objects;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.utils.VariableTable;

import java.util.regex.Pattern;

public class QString extends QObject {

    public static VariableTable defaults = new VariableTable();

    public String value;

    public QString(String value) {
        table.putAll(defaults);
        setObjectMetadata("String");
    }

    public QObject sum(Runtime runtime, QObject other) {
        return QObject.Val(other.toString() + value);
    }

    public QObject subtract(Runtime runtime, QObject other) {
        return QObject.Val(value.replaceAll(Pattern.quote(other.toString()), ""));
    }


}
