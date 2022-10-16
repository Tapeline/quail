package me.tapeline.quailj.typing.utils;

import me.tapeline.quailj.parsing.nodes.Node;

import java.util.HashMap;
import java.util.List;

public class ContainerPreRuntimeContents {

    private List<Node> keys;
    private HashMap<Integer, Node> valueMap;

    public ContainerPreRuntimeContents(List<Node> keys, HashMap<Integer, Node> valueMap) {
        this.keys = keys;
        this.valueMap = valueMap;
    }

    public List<Node> getKeys() {
        return keys;
    }

    public HashMap<Integer, Node> getValueMap() {
        return valueMap;
    }

}
