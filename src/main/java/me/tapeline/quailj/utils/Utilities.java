package me.tapeline.quailj.utils;

import me.tapeline.quailj.parser.nodes.VariableNode;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.types.modifiers.TypeModifier;
import me.tapeline.quailj.types.modifiers.VariableModifier;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Utilities {
    public static HashMap<String, String> opToString = new HashMap<>();

    public static void init() {
        opToString.put("+", "_add");
        opToString.put("-", "_sub");
        opToString.put("//", "_divint");
        opToString.put("/", "_div");
        opToString.put("*", "_mul");
        opToString.put("^", "_pow");
        opToString.put("%", "_mod");
        opToString.put("==", "_cmpeq");
        opToString.put("!=", "_cmpuneq");
        opToString.put("<=", "_cmplet");
        opToString.put(">=", "_cmpget");
        opToString.put(">", "_cmpgt");
        opToString.put("<", "_cmplt");
        opToString.put("get", "_get");
        opToString.put("tostring", "_tostring");
        opToString.put("tonumber", "_tonumber");
        opToString.put("tobool", "_tobool");
        opToString.put("not", "_not");
        opToString.put("!", "_not");
        opToString.put("index", "_index");
        opToString.put("setindex", "_setindex");
        opToString.put("call", "_call");
        opToString.put(">>", "_shr");
        opToString.put("<<", "_shl");
        opToString.put(":", "_range");
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public static boolean isBoolean(String s) {
        return s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false");
    }

    public static BoolType compare(QType a, QType b) {
        if (QType.isBool(a, b))
            return new BoolType( ((BoolType) a).value == ((BoolType) b).value);
        else if (a instanceof BinType && b instanceof BinType)
            return new BoolType(((BinType) a).value == ((BinType) b).value);
        else if (QType.isNum(a, b))
            return new BoolType( ((NumType) a).value == ((NumType) b).value);
        else if (QType.isStr(a, b))
            return new BoolType( ((StringType) a).value.equals(((StringType) b).value));
        else if (QType.isList(a, b))
            return ListUtils.compare(((ListType) a).values, ((ListType) b).values);
        else if (QType.isCont(a, b)) {
            if (a.table.size() != b.table.size()) return new BoolType(false);
            for (String key : a.table.keySet())
                if (!compare(a.table.get(key), b.table.get(key)).value)
                    return new BoolType(false);
            return new BoolType(true);
        }
        return new BoolType(false);
    }

    public static int[] getLine(String text, int pos) {
        String[] lines = text.split("\n");
        int ch = 0;
        for (int i = 0; i < lines.length; i++) {
            ch += lines[i].length();
            if (ch > pos) return new int[] {i + 1, pos - ch};
        }
        return new int[] {lines.length, ch};
    }

    public static QType getDefaultValue(Class clazz) {
        QType q = QType.V();
        if (clazz == BoolType.class) {
            q = QType.V(false);
        } else if (clazz == ContainerType.class) {
            q = new ContainerType(new HashMap<>());
        } else if (clazz == ListType.class) {
            q = new ListType();
        } else if (clazz == NumType.class) {
            q = new NumType(0);
        } else if (clazz == StringType.class) {
            q = new StringType("");
        }
        return q;
    }

    public static QType getDefaultValue(VariableNode n) {
        for (VariableModifier vm : n.modifiers)
            if (vm instanceof TypeModifier)
                return getDefaultValue(((TypeModifier) vm).requiredType);
        return QType.V();
    }
}
