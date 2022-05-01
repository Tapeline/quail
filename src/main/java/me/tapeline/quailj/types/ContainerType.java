package me.tapeline.quailj.types;

import java.util.HashMap;

public class ContainerType extends QType {

    public static HashMap<String, QValue> tableToClone = new HashMap<>();

    public ContainerType(HashMap<String, QValue> content) {
        this.table = new HashMap<>();
        this.table.putAll(tableToClone);
        this.table.putAll(content);
        this.table.put("_ismeta", new QValue(false));
        this.table.put("_name", new QValue("_anonymous"));
        this.table.put("_like", new QValue("container"));
    }

    public ContainerType(String name, String like, HashMap<String, QValue> content, boolean isMeta) {
        this.table = new HashMap<>();
        this.table.putAll(tableToClone);
        this.table.putAll(content);
        this.table.put("_ismeta", new QValue(isMeta));
        this.table.put("_name", new QValue(name));
        this.table.put("_like", new QValue(like));
    }

    public String like() {
        return QValue.nullSafe(this.table.get("_like")).toString();
    }

    public String name() {
        return QValue.nullSafe(this.table.get("_name")).toString();
    }

    public boolean isMeta() {
        QValue m = QValue.nullSafe(this.table.get("_ismeta"));
        return m.v instanceof BoolType ? ((BoolType) m.v).value : false;
    }


    @Override
    public QType copy() {
        HashMap<String, QValue> newTable = new HashMap<>();
        this.table.forEach((k, v) -> newTable.put(k, v.copy()));
        return new ContainerType(
                this.name(),
                this.like(),
                newTable,
                this.isMeta()
        );
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("{");
        for (String key : table.keySet()) {
            if (!key.startsWith("_") && !ContainerType.tableToClone.containsKey(key)) {
                String repr = table.get(key).toString();
                if (table.get(key).v instanceof StringType)
                    repr = '"' + repr + '"';
                if (table.get(key).v instanceof FuncType)
                    repr = "\"func:" + repr + '"';
                if (table.get(key).v instanceof JavaType)
                    repr = "\"java:" + repr + '"';
                if (table.get(key).v instanceof VoidType)
                    repr = "null";
                s.append("\"").append(key).append("\" = ").append(repr).append(", ");
            }
        }
        return s.toString().equals("{")? "{}" : s.substring(0, s.length() - 2) + "}";
    }

    public String allToString() {
        StringBuilder s = new StringBuilder("{");
        for (String key : table.keySet()) {
            String repr = table.get(key).toString();
            if (table.get(key).v instanceof StringType)
                repr = '"' + repr + '"';
            if (table.get(key).v instanceof FuncType)
                repr = "\"func:" + repr + '"';
            if (table.get(key).v instanceof JavaType)
                repr = "\"java:" + repr + '"';
            if (table.get(key).v instanceof VoidType)
                repr = "null";
            s.append("\"").append(key).append("\" = ").append(repr).append(", ");
        }
        return s.toString().equals("{")? "{}" : s.substring(0, s.length() - 2) + "}";
    }

}
