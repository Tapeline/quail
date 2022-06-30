package me.tapeline.quailj.runtime;

import me.tapeline.quailj.parser.nodes.VariableNode;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;
import me.tapeline.quailj.types.VariableModifier;

import java.util.*;
import java.util.function.BiConsumer;

public class VariableTable {

    public Map<String, QType> data = new HashMap<>();
    public Map<String, List<VariableModifier>> mods = new HashMap<>();

    public QType put(Runtime r, String a, QType d) throws RuntimeStriker {
        if (mods.get(a) == null || mods.get(a) != null && VariableNode.match(mods.get(a), r, d)) {
            data.put(a, d);
        } else throw new RuntimeStriker("assign:attempt to assign data with wrong type to clarified variable (" +
                mods.toString() + " = " + d.getClass().getSimpleName() + ")");
        return d;
    }

    public QType put(String a, QType d, List<VariableModifier> m) {
        data.put(a, d);
        mods.put(a, m == null? new ArrayList<>() : m);
        return d;
    }

    public boolean containsKey(String a) {
        return data.containsKey(a);
    }

    public void remove(String a) {
        data.remove(a);
        mods.remove(a);
    }

    public QType get(String a) {
        return data.get(a);
    }

    public void putAll(VariableTable v) {
        v.data.forEach((k, d) -> {
            data.put(k, d);
            mods.put(k, v.mods.get(k));
        });
    }

    public void forEach(BiConsumer<String, QType> c) {
        data.forEach(c);
    }
    public Set<String> keySet() {
        return data.keySet();
    }

    public int size() {
        return data.size();
    }

    public Collection<QType> values() {
        return data.values();
    }

}
