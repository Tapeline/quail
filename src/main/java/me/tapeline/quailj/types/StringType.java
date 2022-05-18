package me.tapeline.quailj.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class StringType extends QType {

    public static HashMap<String, QType> tableToClone = new HashMap<>();

    public String value = "";

    public StringType(String s) {
        value = s;
        this.table = new HashMap<>();
        table.putAll(tableToClone);
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(table);
        oos.writeObject(value);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        table = (HashMap<String, QType>) ois.readObject();
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
