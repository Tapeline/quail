package me.tapeline.quailj.types;

import java.util.HashMap;

public class RefType extends QType {

    public static HashMap<String, QValue> tableToClone = new HashMap<>();

    public QValue object;

    public RefType(QValue object) {
        this.object = object;
        this.table = new HashMap<>();
        table.putAll(tableToClone);
    }

    @Override
    public QType copy() {
        RefType v = new RefType(this.object);
        v.table.putAll(this.table);
        return v;
    }

    @Override
    public String toString() {
        return "&" + object;
    }

}
