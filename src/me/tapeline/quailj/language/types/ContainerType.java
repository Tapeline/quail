package me.tapeline.quailj.language.types;

import java.util.HashMap;

public class ContainerType extends QType {

    public HashMap<String, QType> content = new HashMap<>();
    public String like;
    public String name;
    public boolean isMeta;

    public ContainerType(HashMap<String, QType> content, boolean isMeta) {
        this.isMeta = isMeta;
        this.content = content;
        if (!this.content.containsKey("__ismeta__")) {
            this.content.put("__ismeta__", new BoolType(isMeta));
        } else {
            this.isMeta = ((BoolType) this.content.get("__ismeta__")).value;
        }
    }

    @Override
    public String toString() {
        return (isMeta? "metacontainer" : "container") + content.toString();
    }

}
