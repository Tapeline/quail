package me.tapeline.quailj.types;

import java.util.HashMap;

public class ContainerType extends QType {

    public static HashMap<String, QType> tableToClone = new HashMap<>();


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
        return new ContainerType(
                this.name(),
                this.like(),
                this.table,
                this.isMeta()
        );
    }

    @Override
    public String toString() {
        return table.toString();
    }

}
