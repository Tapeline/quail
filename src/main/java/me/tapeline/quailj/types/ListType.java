package me.tapeline.quailj.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(table);
        oos.writeObject(values);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        table = (HashMap<String, QType>) ois.readObject();
        values = (List<QType>) ois.readObject();
    }

    @Override
    public QType copy() {
        ListType vv = new ListType();
        for (QType q : this.values)
            vv.values.add(q.copy());
        HashMap<String, QType> newTable = new HashMap<>();
        this.table.forEach((k, v) -> newTable.put(k, v.copy()));
        vv.table.putAll(newTable);
        return vv;
    }

    @Override
    public String toString() {
        return values.toString();
    }


}
