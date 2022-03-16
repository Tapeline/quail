package me.tapeline.quailj.types;

import java.util.HashMap;

public class BoolType implements QType {

    public static HashMap<String, QType> tableToClone = new HashMap<>();

    public HashMap<String, QType> table = new HashMap<>();
    public boolean value = false;

    public BoolType(boolean b) {
        value = b;
        table.putAll(tableToClone);
    }

    @Override
    public String toString() {
        return value? "true" : "false";
    }


}
