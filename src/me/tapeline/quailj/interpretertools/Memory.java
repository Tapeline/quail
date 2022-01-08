package me.tapeline.quailj.interpretertools;

import me.tapeline.quailj.language.types.QType;
import me.tapeline.quailj.language.types.RefType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

public class Memory {

    public Map<String, QType> mem = new HashMap<>();
    public List<String> finalized = new ArrayList<>();
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
        if (finalized.contains(a)) return;
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
            QType r = mem.get(a);
            if (r instanceof RefType)
                return get(((RefType) r).value);
            return r;
        }
    }

    public void finalize(String a) {
        if (hasParentalDefinition(a)) enclosing.finalize(a);
        else if (!finalized.contains(a)) finalized.add(a);
    }

    public String dump() {
        return "MemoryDump:\nMemory: " + mem.toString() + "\nFinalized (Blocked): " +
                finalized.toString() + "\nEnclosing: " + enclosing.dump();
    }
}
