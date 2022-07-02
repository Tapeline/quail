package me.tapeline.quailj.types;

import me.tapeline.quailj.runtime.VariableTable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class VoidType extends QType {

    public static VariableTable tableToClone = new VariableTable("Default VoidType");

    public VoidType() {
        this.table = new VariableTable();
        table.putAll(tableToClone);
    }

    @Override
    public QType copy() {
        VoidType v = new VoidType();
        v.table.putAll(this.table);
        return v;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(table);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        table = (VariableTable) ois.readObject();
    }

    @Override
    public String toString() {
        return "null";
    }

}
