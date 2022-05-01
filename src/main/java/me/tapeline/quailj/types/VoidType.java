package me.tapeline.quailj.types;

import java.util.HashMap;

public class VoidType extends QType {

    public static HashMap<String, QValue> tableToClone = new HashMap<>();

    public VoidType() {
        this.table = new HashMap<>();
        table.putAll(tableToClone);
    }

    @Override
    public QType copy() {
        VoidType v = new VoidType();
        v.table.putAll(this.table);
        return v;
    }

    @Override
    public String toString() {
        return "null";
    }

}
