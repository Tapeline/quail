package me.tapeline.quailj.types;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class ContainerType extends QType {

    public static HashMap<String, QType> tableToClone = new HashMap<>();

    public ContainerType(HashMap<String, QType> content) {
        this.table = new HashMap<>();
        this.table.putAll(tableToClone);
        this.table.putAll(content);
        this.table.put("_ismeta", new BoolType(false));
        this.table.put("_name", new StringType("_anonymous"));
        this.table.put("_like", new StringType("container"));
    }

    public ContainerType(String name, String like, HashMap<String, QType> content, boolean isMeta) {
        this.table = new HashMap<>();
        this.table.putAll(tableToClone);
        this.table.putAll(content);
        this.table.put("_ismeta", new BoolType(isMeta));
        this.table.put("_name", new StringType(name));
        this.table.put("_like", new StringType(like));
    }

    public String like() {
        return QType.nullSafe(this.table.get("_like")).toString();
    }

    public String name() {
        return QType.nullSafe(this.table.get("_name")).toString();
    }

    public boolean isMeta() {
        QType m = QType.nullSafe(this.table.get("_ismeta"));
        return m instanceof BoolType? ((BoolType) m).value : false;
    }


    @Override
    public QType copy() {
        HashMap<String, QType> newTable = new HashMap<>();
        this.table.forEach((k, v) -> {
            newTable.put(k, v.copy());
        });
        return new ContainerType(
                this.name(),
                this.like(),
                newTable,
                this.isMeta()
        );
    }

    @Override
    public String toString() {
        String s = "{";
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
                s += "\"" + key + "\" = " + repr + ", ";
            }
        }
        return s.equals("{")? "{}" : s.substring(0, s.length() - 2) + "}";
    }

    public String allToString() {
        String s = "{";
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
            s += "\"" + key + "\" = " + repr + ", ";
        }
        return s.equals("{")? "{}" : s.substring(0, s.length() - 2) + "}";
    }

}
