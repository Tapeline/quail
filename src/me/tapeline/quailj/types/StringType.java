package me.tapeline.quailj.types;

import java.util.HashMap;

public class StringType extends QType {

    public static HashMap<String, QType> tableToClone = new HashMap<>();

    public String value = "";

    public StringType(String s) {
        value = s;
        this.table = new HashMap<>();
        table.putAll(tableToClone);
    }

    @Override
    public QType copy() {
        StringType v = new StringType(this.value);
        v.table.putAll(this.table);
        return v;
    }

    @Override
    public String toString() {
        return value;
    }


}
