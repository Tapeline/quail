package me.tapeline.quailj.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class RefType extends QType {

    public static HashMap<String, QType> tableToClone = new HashMap<>();

    public QType object;

    public RefType(QType object) {
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

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(table);
        oos.writeObject(object);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        table = (HashMap<String, QType>) ois.readObject();
        object = (QType) ois.readObject();
    }

    @Override
    public String toString() {
        return "&" + object;
    }

}
