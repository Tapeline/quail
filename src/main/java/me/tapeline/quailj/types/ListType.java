package me.tapeline.quailj.types;

import java.util.*;

public class ListType extends QType {

    public static HashMap<String, QType> tableToClone = new HashMap<>();

    public List<QType> values = new ArrayList<>();

    public ListType(List<QType> l) {
        values = l;
        this.table = new HashMap<>();
        table.putAll(tableToClone);
    }

    public ListType(QType[] l) {
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
        for (QType q : this.values)
            vv.values.add(q.copy());
        HashMap<String, QType> newTable = new HashMap<>();
        this.table.forEach((k, v) -> {
            newTable.put(k, v.copy());
        });
        vv.table.putAll(newTable);
        return vv;
    }

    @Override
    public String toString() {
        return values.toString();
    }


}
