package me.tapeline.quailj.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BinType extends QType {

    public byte value;

    public BinType(byte v) {
        value = v;
        table = null;
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeDouble(value);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        value = ois.readByte();
    }

    @Override
    public QType copy() {
        return new BinType(this.value);
    }

    @Override
    public String toString() {
        return Integer.toString(value);
    }
}
