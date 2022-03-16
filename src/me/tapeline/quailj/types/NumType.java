package me.tapeline.quailj.types;

import java.util.HashMap;

public class NumType implements QType {

    public static HashMap<String, QType> tableToClone = new HashMap<>();

    public HashMap<String, QType> table = new HashMap<>();
    public double value = 0D;

    public NumType(double d) {
        value = d;
        table.putAll(tableToClone);
    }

    @Override
    public String toString() {
        // Round value if possible
        return value % 1 == 0? Long.toString(Math.round(value)) : Double.toString(value);
    }

}
