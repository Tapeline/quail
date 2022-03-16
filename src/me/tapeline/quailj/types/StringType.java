package me.tapeline.quailj.types;

import java.util.HashMap;

public class StringType implements QType {

    public static HashMap<String, QType> tableToClone = new HashMap<>();

    public HashMap<String, QType> table = new HashMap<>();
    public String value = "";

    public StringType(String s) {
        value = s;
        table.putAll(tableToClone);
    }

    @Override
    public String toString() {
        return value;
    }


}
