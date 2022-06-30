package me.tapeline.quailj.types;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.VariableTable;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ContainerType extends QType {

    public static VariableTable tableToClone = new VariableTable();

    public ContainerType(VariableTable content) {
        this.table = new VariableTable();
        this.table.putAll(tableToClone);
        this.table.putAll(content);
        this.table.put("_ismeta", new BoolType(false), new ArrayList<>());
        this.table.put("_name", new StringType("_anonymous"), new ArrayList<>());
        this.table.put("_like", new StringType("container"), new ArrayList<>());
    }

    public ContainerType(HashMap<String, QType> content) {
        this.table = new VariableTable();
        this.table.putAll(tableToClone);
        this.table.data.putAll(content);
        this.table.put("_ismeta", new BoolType(false), new ArrayList<>());
        this.table.put("_name", new StringType("_anonymous"), new ArrayList<>());
        this.table.put("_like", new StringType("container"), new ArrayList<>());
    }

    public ContainerType(String name, String like, VariableTable content, boolean isMeta) {
        this.table = new VariableTable();
        this.table.putAll(tableToClone);
        this.table.putAll(content);
        this.table.put("_ismeta", new BoolType(isMeta), new ArrayList<>());
        this.table.put("_name", new StringType(name), new ArrayList<>());
        this.table.put("_like", new StringType(like), new ArrayList<>());
    }

    public ContainerType(String name, String like, HashMap<String, QType> content, boolean isMeta) {
        this.table = new VariableTable();
        this.table.putAll(tableToClone);
        this.table.data.putAll(content);
        this.table.put("_ismeta", new BoolType(isMeta), new ArrayList<>());
        this.table.put("_name", new StringType(name), new ArrayList<>());
        this.table.put("_like", new StringType(like), new ArrayList<>());
    }

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeObject(table);
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        table = (VariableTable) ois.readObject();
    }

    public String like() {
        return QType.nullSafe(this.table.get("_like")).toString();
    }

    public String name() {
        return QType.nullSafe(this.table.get("_name")).toString();
    }

    public boolean isMeta() {
        QType m = QType.nullSafe(this.table.get("_ismeta"));
        return m instanceof BoolType ? ((BoolType) m).value : false;
    }


    @Override
    public QType copy() {
        VariableTable newTable = new VariableTable();
        this.table.forEach((k, v) -> newTable.put(k, v.copy(), table.mods.get(k)));
        return new ContainerType(
                this.name(),
                this.like(),
                newTable,
                this.isMeta()
        );
    }

    @Override
    public String toString() {
        /*StringBuilder s = new StringBuilder("{");
        for (String key : table.keySet()) {
            if (!key.startsWith("_") && !ContainerType.tableToClone.containsKey(key)) {
                String repr = table.get(key).toString();
                if (table.get(key) instanceof StringType)
                    repr = '"' + repr + '"';
                if (table.get(key) instanceof FuncType)
                    repr = "\"func:" + repr + '"';
                if (table.get(key) instanceof JavaType)
                    repr = "\"java:" + repr + '"';
                if (table.get(key) instanceof VoidType)
                    repr = "null";
                if (table.get(key) instanceof ContainerType)
                    repr = ((ContainerType) table.get(key)).name() +
                            "@" + table.get(key).hashCode();
                s.append("\"").append(key).append("\" = ").append(repr).append(", ");
            }
        }
        return s.toString().equals("{")? "{}" : s.substring(0, s.length() - 2) + "}";*/
        return this.name() + ":" + this.like() + "@" + this.hashCode();
    }

    public String allToString() {
        StringBuilder s = new StringBuilder("{");
        for (String key : table.keySet()) {
            String repr = table.get(key).toString();
            if (table.get(key) instanceof StringType)
                repr = '"' + repr + '"';
            if (table.get(key) instanceof FuncType)
                repr = "\"func:" + repr + '"';
            if (table.get(key) instanceof JavaType)
                repr = "\"java:" + repr + '"';
            if (table.get(key) instanceof VoidType)
                repr = "null";
            s.append("\"").append(key).append("\" = ").append(repr).append(", ");
        }
        return s.toString().equals("{")? "{}" : s.substring(0, s.length() - 2) + "}";
    }

    public boolean isInstance(Runtime r, String s) {
        if (name().equals(s)) return true;
        if (like().equals(s)) return true;
        if (r.scope.get(like()) instanceof ContainerType &&
                ((ContainerType) r.scope.get(like())).isInstance(r, s))
            return true;
        return false;
    }

}
