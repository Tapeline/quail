package me.tapeline.quailj.utils;

import me.tapeline.quailj.language.types.*;

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

    public static int getLine(int pos, String code) {
        List<String> split = Arrays.asList(code.split("\n"));
        int sum = 0;
        for (int i = 0; i < split.size(); i++) {
            sum += split.get(i).length();
            if (sum <= pos && sum + split.get(i+1).length() > pos)
                return i;
        }
        return 1;
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
            return new BoolType( ((ContainerType) a).isMeta == ((ContainerType) b).isMeta);
        }
        return null;
    }
}
