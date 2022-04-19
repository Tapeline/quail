package me.tapeline.quailj.runtime;

import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.VoidType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Memory {

    public Map<String, QType> mem = new HashMap<>();
    public Memory enclosing;

    public Memory() {
        this.enclosing = null;
    }

    public Memory(Memory m) {
        this.enclosing = m;
    }

    public boolean hasParentalDefinition(String key) {
        if (enclosing != null) return enclosing.hasParentalDefinition(key);
        else return mem.containsKey(key);
    }

    public void set(String a, QType val) {
        if (enclosing != null) {
            if (hasParentalDefinition(a)) enclosing.set(a, val);
            else mem.put(a, val);
        } else mem.put(a, val);
    }

    public void remove(String a) {
        if (enclosing != null) {
            if (hasParentalDefinition(a)) enclosing.remove(a);
            else mem.remove(a);
        } else mem.remove(a);
    }

    public QType get(String a) {
        if (hasParentalDefinition(a) && enclosing != null) return enclosing.get(a);
        else {
            return QType.nullSafe(mem.get(a));
        }
    }


    public String dump() {
        return "MemoryDump:\nMemory: " + mem.toString() + "\nEnclosing: " + (enclosing != null? enclosing.dump() : "{}");
    }
}

