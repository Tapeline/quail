package me.tapeline.quailj.language.types;

import java.util.HashMap;

public class ContainerType extends QType {

    public HashMap<String, QType> content = new HashMap<>();
    public HashMap<String, FuncType> customBehaviour = new HashMap<>();
    public FuncType builder;
    public String like;
    public String name;
    public boolean isMeta = false;

    public ContainerType(HashMap<String, QType> content, boolean isMeta) {
        this.isMeta = isMeta;
        this.content = content;
    }

    public ContainerType(HashMap<String, QType> content, boolean isMeta, HashMap<String, FuncType> customBehaviour) {
        this.isMeta = isMeta;
        this.content = content;
        this.customBehaviour = customBehaviour;
    }

    public ContainerType(HashMap<String, QType> content, boolean isMeta, HashMap<String, FuncType> customBehaviour,
                         FuncType builder) {
        this.isMeta = isMeta;
        this.content = content;
        this.customBehaviour = customBehaviour;
        this.builder = builder;
    }

    @Override
    public String toString() {
        return (isMeta? "metacontainer" : "container") + content.toString();
    }

}
