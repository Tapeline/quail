package me.tapeline.quailj.typing.utils;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;
import me.tapeline.quailj.typing.modifiers.FinalModifier;
import me.tapeline.quailj.typing.modifiers.VariableModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;

import java.util.*;
import java.util.function.BiConsumer;

public class VariableTable {

    private final HashMap<String, QObject> values = new HashMap<>();
    public Map<String, List<VariableModifier>> modifiers = new HashMap<>();

    public QObject put(Runtime runtime, String id, QObject value) throws RuntimeStriker {
        // TODO: optimization
        if (modifiers.get(id) == null ||
                modifiers.get(id) != null && VariableNode.match(modifiers.get(id), runtime, value)) {
            if (modifiers.get(id) != null)
                for (VariableModifier vm : modifiers.get(id))
                    if (vm instanceof FinalModifier) {
                        if (((FinalModifier) vm).hadAssignment)
                            Runtime.error("Attempt to assign data to final variable");
                        else
                            ((FinalModifier) vm).hadAssignment = true;
                    }
            values.put(id, value);
        } else
            Runtime.error("Attempt to assign data with wrong type to clarified variable (" +
                modifiers.get(id).toString() + " = " + value.getClassName() + ")");
        return value;
    }

    public QObject put(String id, QObject value, List<VariableModifier> modifiers) {
        values.put(id, value);
        this.modifiers.put(id, modifiers == null? new ArrayList<>() : modifiers);
        return value;
    }

    public QObject put(String id, QObject value) {
        List<VariableModifier> m = new ArrayList<>();
        values.put(id, value);
        modifiers.put(id, new ArrayList<>());
        return value;
    }

    public boolean containsKey(String id) {
        return values.containsKey(id);
    }

    public void remove(String id) {
        values.remove(id);
        modifiers.remove(id);
    }

    public QObject get(String id) {
        return values.get(id);
    }

    public void putAll(VariableTable table) {
        table.values.forEach((k, d) -> {
            values.put(k, d);
            modifiers.put(k, table.modifiers.get(k));
        });
    }

    public void putAll(HashMap<String, QObject> table) {
        values.putAll(table);
    }

    public void forEach(BiConsumer<String, QObject> consumer) {
        values.forEach(consumer);
    }
    public Set<String> keySet() {
        return values.keySet();
    }

    public int size() {
        return values.size();
    }

    public Collection<QObject> values() {
        return values.values();
    }

    public HashMap<String, QObject> getValues() {
        return values;
    }

    public List<VariableModifier> getModifiersFor(String key) {
        return modifiers.get(key);
    }

}
