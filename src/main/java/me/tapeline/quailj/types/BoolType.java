package me.tapeline.quailj.types;

import java.util.HashMap;

public class BoolType extends QType {

    public static HashMap<String, QType> tableToClone = new HashMap<>();
    public boolean value = false;

    public BoolType(boolean b) {
        value = b;
        this.table = new HashMap<>();
        table.putAll(tableToClone);
    }

    @Override
    public QType copy() {
        BoolType v = new BoolType(this.value);
        v.table.putAll(this.table);
        return v;
    }

    @Override
    public String toString() {
        return value? "true" : "false";
    }


}
