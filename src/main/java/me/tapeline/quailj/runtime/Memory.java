package me.tapeline.quailj.runtime;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.variable.VariableNode;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.modifiers.VariableModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.utils.VariableTable;

import java.util.ArrayList;
import java.util.List;

public class Memory {

    public VariableTable mem = new VariableTable();
    public Memory enclosing;

    public Memory() {
        this.enclosing = null;
    }

    public Memory(Memory m) {
        this.enclosing = m;
    }

    public boolean contains(String key) {
        if (mem.containsKey(key)) return true;
        if (enclosing != null) return enclosing.contains(key);
        return false;
    }

    public void set(Runtime runtime, String id, QObject value) throws RuntimeStriker {
        if (enclosing != null) {
            if (enclosing.contains(id))
                enclosing.set(runtime, id, value);
            else
                mem.put(runtime, id, value);
        } else mem.put(runtime, id, value);
    }

    public void set(String id, QObject value, List<VariableModifier> modifiers) {
        mem.put(id, value, modifiers);
    }

    public QObject get(String id) {
        QObject value = null;
        if (mem.getValues().containsKey(id))
            value = mem.get(id);
        else if (enclosing != null) value = enclosing.get(id);

        if (value == null) {
            QObject q = QObject.Val();
            mem.put(id, q, new ArrayList<>());
            return q;
        } else return value;
    }

    public QObject get(String id, VariableNode node) {
        QObject alreadyUsed = null;
        if (mem.getValues().containsKey(id))
            alreadyUsed = mem.get(id);
        else if (enclosing != null) alreadyUsed = enclosing.get(id);
        if (alreadyUsed == null) {
            QObject newObject = QObject.Val();
            for (VariableModifier m : node.modifiers) {
                if (m instanceof TypeModifier) {
                    if (((TypeModifier) m).possibleType == TokenType.TYPE_BOOL)
                        newObject = QObject.Val(false);
                    else if (((TypeModifier) m).possibleType == TokenType.TYPE_LIST)
                        newObject = QObject.Val(new ArrayList<>());
                    else if (((TypeModifier) m).possibleType == TokenType.TYPE_NUM)
                        newObject = QObject.Val(0);
                    else if (((TypeModifier) m).possibleType == TokenType.TYPE_STRING)
                        newObject = QObject.Val("");
                }
            }
            mem.put(id, newObject, node.modifiers);
            return newObject;
        } else return alreadyUsed;
    }

}

