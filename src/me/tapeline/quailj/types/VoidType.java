package me.tapeline.quailj.types;

import java.util.HashMap;

public class VoidType implements QType {

    public static HashMap<String, QType> tableToClone = new HashMap<>();

    public HashMap<String, QType> table = new HashMap<>();

    public VoidType() {
        table.putAll(tableToClone);
    }

    @Override
    public String toString() {
        return "null";
    }

}
