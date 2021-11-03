package me.tapeline.quarkj.interpretertools;

import me.tapeline.quarkj.language.types.QType;
import me.tapeline.quarkj.language.types.VoidType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Memory {

    public HashMap<String, QType> mem = new HashMap<>();
    public List<String> finalized = new ArrayList<>();

    public Memory() {
        mem.put("null", new VoidType(true));
        finalized.add("null");
    }

    public void set(String a, QType val) {
        if (!finalized.contains(a)) mem.put(a, val);
    }

    public QType get(String a) {
        return mem.get(a);
    }

    public void finalize(String a) {
        finalized.add(a);
    }
}
