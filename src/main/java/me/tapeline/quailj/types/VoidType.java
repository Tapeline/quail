package me.tapeline.quailj.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class VoidType extends QType {

    public static HashMap<String, QType> tableToClone = new HashMap<>();

    public VoidType() {
        this.table = new HashMap<>();
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
        table = (HashMap<String, QType>) ois.readObject();
    }

    @Override
    public String toString() {
        return "null";
    }

}
