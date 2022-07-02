package me.tapeline.quailj.runtime;

import me.tapeline.quailj.parser.nodes.VariableNode;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.types.QType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Memory {

    public VariableTable mem = new VariableTable("global");
    public Memory enclosing;

    public Memory() {
        this.enclosing = null;
    }

    public Memory(Memory m) {
        this.enclosing = m;
        m.mem.scope = "enclosing of " + mem.scope;
    }

    public boolean hasParentalDefinition(String key) {
        if (enclosing != null) return enclosing.hasParentalDefinition(key);
        else return mem.containsKey(key);
    }

    public void set(Runtime r, String a, QType val) throws RuntimeStriker {
        if (enclosing != null) {
            if (hasParentalDefinition(a)) enclosing.set(r, a, val);
            else mem.put(r, a, val);
        } else mem.put(r, a, val);
    }

    public void set(String a, QType val, List<VariableModifier> m) {
        mem.put(a, val, m);
    }

    public void remove(String a) {
        if (enclosing != null) {
            if (hasParentalDefinition(a)) enclosing.remove(a);
            else mem.remove(a);
        } else mem.remove(a);
    }

    public QType get(String a) {
        QType v = null;
        if (enclosing != null && hasParentalDefinition(a)) v = enclosing.get(a);
        else v = mem.get(a);
        if (v == null) {
            QType q = QType.V();
            mem.put(a, q, new ArrayList<>());
            return q;
        } else return v;
    }

    public QType get(String a, VariableNode n) {
        QType v = null;
        if (enclosing != null && hasParentalDefinition(a)) v = enclosing.get(a);
        else v = mem.get(a);
        if (v == null) {
            QType q = QType.V();
            for (VariableModifier m : n.modifiers) {
                if (m instanceof TypeModifier) {
                    if (((TypeModifier) m).requiredType == BoolType.class) {
                        q = QType.V(false);
                    } else if (((TypeModifier) m).requiredType == ContainerType.class) {
                        q = new ContainerType(new HashMap<>());
                    } else if (((TypeModifier) m).requiredType == ListType.class) {
                        q = new ListType();
                    } else if (((TypeModifier) m).requiredType == NumType.class) {
                        q = new NumType(0);
                    } else if (((TypeModifier) m).requiredType == StringType.class) {
                        q = new StringType("");
                    }
                }
            }
            mem.put(a, q, n.modifiers);
            return q;
        } else return v;
    }


    public String dump() {
        return "MemoryDump:\nMemory: " + mem.toString() + "\nEnclosing: " + (enclosing != null? enclosing.dump() : "{}");
    }
}

