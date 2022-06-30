package me.tapeline.quailj.types;

import me.tapeline.quailj.runtime.VariableTable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class StringType extends QType {

    public static VariableTable tableToClone = new VariableTable();

    public String value = "";

    public StringType(String s) {
        value = s;
        this.table = new VariableTable();
        table.putAll(tableToClone);
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(table);
        oos.writeObject(value);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        table = (VariableTable) ois.readObject();
        value = (String) ois.readObject();
    }

    @Override
    public QType copy() {
        StringType v = new StringType(this.value);
        v.table.putAll(this.table);
        return v;
    }

    @Override
    public String toString() {
        return value;
    }


}
