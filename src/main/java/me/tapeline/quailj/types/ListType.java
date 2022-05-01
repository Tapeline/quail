package me.tapeline.quailj.types;

import java.util.*;

public class ListType extends QType {

    public static HashMap<String, QValue> tableToClone = new HashMap<>();

    public List<QValue> values = new ArrayList<>();

    public ListType(List<QValue> l) {
        values = l;
        this.table = new HashMap<>();
        table.putAll(tableToClone);
    }

    public ListType(QValue[] l) {
        values = Arrays.asList(l);
        this.table = new HashMap<>();
        table.putAll(tableToClone);
    }

    public ListType() {
        values = new ArrayList<>();
        this.table = new HashMap<>();
        table.putAll(tableToClone);
    }

    @Override
    public QType copy() {
        ListType vv = new ListType();
        for (QValue q : this.values)
            vv.values.add(q.copy());
        HashMap<String, QValue> newTable = new HashMap<>();
        this.table.forEach((k, v) -> newTable.put(k, v.copy()));
        vv.table.putAll(newTable);
        return vv;
    }

    @Override
    public String toString() {
        return values.toString();
    }


}
