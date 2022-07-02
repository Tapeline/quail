package me.tapeline.quailj.types;

import me.tapeline.quailj.runtime.VariableTable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

public class BoolType extends QType implements Serializable {

    public static VariableTable tableToClone = new VariableTable("Default BoolType");
    public boolean value = false;

    public BoolType(boolean b) {
        value = b;
        this.table = new VariableTable();
        table.putAll(tableToClone);
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(table);
        oos.writeBoolean(value);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        table = (VariableTable) ois.readObject();
        value = ois.readBoolean();
    }

    @Override
    public QType copy() {
        BoolType v = new BoolType(this.value);
        v.table.putAll(this.table);
        return v;
    }

    @Override
    public String toString() {
        return value? "true" : "false";
    }


}
