package me.tapeline.quarkj.interpretertools;

import me.tapeline.quarkj.language.types.QType;

import java.util.HashMap;

public class Memory {

    public HashMap<String, QType> mem = new HashMap<>();

    public void set(String a, QType val) {
        mem.put(a, val);
    }
}
