package me.tapeline.quailj.utils;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.*;

import static me.tapeline.quailj.lexing.TokenType.*;

public class Utilities {

    public static HashMap<TokenType, String> opToString = new HashMap<>();

    public static void init() {
        opToString.put(PLUS, "_add");
        opToString.put(MINUS, "_sub");
        opToString.put(INTDIV, "_intdiv");
        opToString.put(DIVIDE, "_div");
        opToString.put(MULTIPLY, "_mul");
        opToString.put(POWER, "_pow");
        opToString.put(MODULO, "_mod");
        opToString.put(EQUALS, "_eq");
        opToString.put(NOT_EQUALS, "_neq");
        opToString.put(LESS_EQUAL, "_cmple");
        opToString.put(GREATER_EQUAL, "_cmpge");
        opToString.put(GREATER, "_cmpg");
        opToString.put(LESS, "_cmpl");
        opToString.put(TYPE_STRING, "_tostring");
        opToString.put(TYPE_NUM, "_tonumber");
        opToString.put(TYPE_BOOL, "_tobool");
        opToString.put(NOT, "_not");
        opToString.put(SHIFT_LEFT, "_shr");
        opToString.put(SHIFT_RIGHT, "_shl");
    }

    public static String collectionToString(Collection<?> collection, String separator) {
        StringBuilder sb = new StringBuilder();
        for (Object element : collection)
            sb.append(element.toString()).append(separator);
        if (sb.toString().endsWith(separator))
            sb.delete(sb.length() - separator.length(), sb.length());
        return sb.toString();
    }

    public static <T> T[] arrayAppend(T[] arr, T element) {
        final int N = arr.length;
        arr = Arrays.copyOf(arr, N + 1);
        arr[N] = element;
        return arr;
    }

    @SafeVarargs
    public static <T> List<T> asList(T... elements) {
        return new ArrayList<>(Arrays.asList(elements));
    }

}
