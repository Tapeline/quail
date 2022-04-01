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
        table.putAll(tableToClone);
    }

    @Override
    public QType copy() {
        ListType v = new ListType(this.values);
        v.table.putAll(this.table);
        return v;
    }

    @Override
    public String toString() {
        return values.toString();
    }


}
