package me.tapeline.quailj.typing.utils;

import me.tapeline.quailj.lexing.TokenType;

import java.util.HashMap;

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
        opToString.put(EQUALS, "_cmpeq");
        opToString.put(NOT_EQUALS, "_cmpuneq");
        opToString.put(LESS_EQUAL, "_cmplet");
        opToString.put(GREATER_EQUAL, "_cmpget");
        opToString.put(GREATER, "_cmpgt");
        opToString.put(LESS, "_cmplt");
        opToString.put(TYPE_STRING, "_tostring");
        opToString.put(TYPE_NUM, "_tonumber");
        opToString.put(TYPE_BOOL, "_tobool");
        opToString.put(NOT, "_not");
        opToString.put(SHIFT_LEFT, "_shr");
        opToString.put(SHIFT_RIGHT, "_shl");
    }
}
