package me.tapeline.quailj.types;

import me.tapeline.quailj.runtime.VariableTable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

public class ListType extends QType {

    public static VariableTable tableToClone = new VariableTable();

    public List<QType> values = new ArrayList<>();

    public ListType(List<QType> l) {
        values = l;
        this.table = new VariableTable();
        table.putAll(tableToClone);
    }

    public ListType(QType[] l) {
        values = Arrays.asList(l);
        this.table = new VariableTable();
        table.putAll(tableToClone);
    }

    public ListType() {
        values = new ArrayList<>();
        this.table = new VariableTable();
        table.putAll(tableToClone);
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(table);
        oos.writeObject(values);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        table = (VariableTable) ois.readObject();
        values = (List<QType>) ois.readObject();
    }

    @Override
    public QType copy() {
        ListType vv = new ListType();
        for (QType q : this.values)
            vv.values.add(q.copy());
        VariableTable newTable = new VariableTable();
        this.table.forEach((k, v) -> newTable.put(k, v.copy(), table.mods.get(k)));
        vv.table.putAll(newTable);
        return vv;
    }

    @Override
    public String toString() {
        return values.toString();
    }


}
