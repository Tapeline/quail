package me.tapeline.quailj.types;

import java.util.HashMap;

public class NumType extends QType {

    public static HashMap<String, QValue> tableToClone = new HashMap<>();

    public double value = 0D;

    public NumType(double d) {
        value = d;
        this.table = new HashMap<>();
        table.putAll(tableToClone);
    }

    @Override
    public QType copy() {
        NumType v = new NumType(this.value);
        v.table.putAll(this.table);
        return v;
    }

    @Override
    public String toString() {
        // Round value if possible
        return value % 1 == 0? Long.toString(Math.round(value)) : Double.toString(value);
    }

}
