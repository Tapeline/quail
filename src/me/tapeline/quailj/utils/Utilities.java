package me.tapeline.quailj.utils;

import me.tapeline.quailj.types.*;

import java.util.Arrays;
import java.util.List;

public class Utilities {
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

    public static int[] getLine(int pos, String code) {
        List<String> split = Arrays.asList(code.split("\n"));
        int sum = 0;
        for (int i = 0; i < split.size(); i++) {
            sum += split.get(i).length();
            if (sum <= pos && sum + split.get(i+1).length() > pos)
                return new int[] {i, pos - sum + 1};
        }
        return new int[] {1, pos};
    }

    public static BoolType compare(QType a, QType b) {
        if (a instanceof BoolType && b instanceof BoolType)
            return new BoolType( ((BoolType) a).value == ((BoolType) b).value);
        else if (a instanceof NumType && b instanceof NumType)
            return new BoolType( ((NumType) a).value == ((NumType) b).value);
        else if (a instanceof StringType && b instanceof StringType)
            return new BoolType( ((StringType) a).value.equals(((StringType) b).value));
        else if (a instanceof ListType && b instanceof ListType)
            return ListUtils.compare(((ListType) a).values, ((ListType) b).values);
        else if (a instanceof ContainerType && b instanceof ContainerType) {
            return new BoolType( ((ContainerType) a).isMeta() == ((ContainerType) b).isMeta());
        }
        return null;
    }

    public static String transformOp(String bh) {
        bh = bh.replaceAll("\\+", "_add");
        bh = bh.replaceAll("-", "_sub");
        bh = bh.replaceAll("//", "_divint");
        bh = bh.replaceAll("/", "_div");
        bh = bh.replaceAll("\\*", "_mul");
        bh = bh.replaceAll("\\^", "_pow");
        bh = bh.replaceAll("%", "_mod");
        bh = bh.replaceAll("==", "_cmpeq");
        bh = bh.replaceAll("!=", "_cmpuneq");
        bh = bh.replaceAll("<=", "_cmplet");
        bh = bh.replaceAll(">=", "_cmpget");
        bh = bh.replaceAll(">", "_cmpgt");
        bh = bh.replaceAll("<", "_cmplt");
        bh = bh.replaceAll("get", "_get");
        bh = bh.replaceAll("tostring", "_tostring_");
        bh = bh.replaceAll("tonumber", "_tonumber");
        bh = bh.replaceAll("tobool", "_tobool");
        bh = bh.replaceAll("not", "_not");
        bh = bh.replaceAll("!", "_not");
        bh = bh.replaceAll("set", "_set");
        return bh;
    }
}
