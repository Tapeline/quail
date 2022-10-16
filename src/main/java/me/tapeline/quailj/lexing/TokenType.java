package me.tapeline.quailj.lexing;

import me.tapeline.quailj.utils.Dict;
import me.tapeline.quailj.utils.Pair;

import java.util.HashMap;

public enum TokenType {

    CONTROL_THROUGH,
    CONTROL_IF,
    CONTROL_ELSEIF,
    CONTROL_ELSE,
    CONTROL_TRY,
    CONTROL_CATCH,
    CONTROL_WHILE,
    CONTROL_LOOP,
    CONTROL_STOP_WHEN,
    CONTROL_EVERY,
    CONTROL_ON,
    CONTROL_WHEN,
    CONTROL_FOR,

    TYPE_FUNC,
    TYPE_STRING,
    TYPE_BOOL,
    TYPE_NUM,
    TYPE_CONTAINER,
    TYPE_LIST,
    TYPE_OBJECT,
    TYPE_FUNCTION,
    TYPE_METHOD,
    TYPE_CLASS,
    TYPE_VOID,

    MOD_REQUIRE,
    MOD_ANYOF,
    MOD_LOCAL,
    MOD_FINAL,
    MOD_STATIC,

    EFFECT_ASSERT,
    EFFECT_USE,
    EFFECT_THROW,
    EFFECT_IMPORT,
    EFFECT_STRIKE,
    EFFECT_RETURN,

    INSTRUCTION_BREAK,
    INSTRUCTION_BREAKPOINT,
    INSTRUCTION_CONTINUE,

    PLUS,
    MINUS,
    MULTIPLY,
    DIVIDE,
    MODULO,
    INTDIV,
    POWER,
    SHIFT_LEFT,
    SHIFT_RIGHT,
    ASSIGN,
    EQUALS,
    NOT_EQUALS,
    GREATER,
    LESS,
    GREATER_EQUAL,
    LESS_EQUAL,
    AND,
    OR,
    RANGE,
    RANGE_INCLUDE,

    SHORT_PLUS,
    SHORT_MINUS,
    SHORT_MULTIPLY,
    SHORT_DIVIDE,
    SHORT_MODULO,
    SHORT_INTDIV,
    SHORT_POWER,
    SHORT_SHIFT_LEFT,
    SHORT_SHIFT_RIGHT,

    NOT,
    HASH,

    // COMMENT,

    LITERAL_NULL,
    LITERAL_STR,
    LITERAL_NUM,
    LITERAL_FALSE,
    LITERAL_TRUE,

    CONSTRUCTOR,
    OVERRIDE,
    SETS,
    GETS,
    ANONYMOUS,

    AS,

    ASYNC,

    JAVA_EMBED,

    LPAR,
    RPAR,
    LSPAR,
    RSPAR,
    LCPAR,
    RCPAR,

    CONSUMER,
    KWARG_CONSUMER,
    LAMBDA_ARROW,
    PILLAR,
    COMMA,
    DOT,
    ID,
    IN,
    INSTANCEOF,
    LIKE,
    EOF,
    EOL;

    public static final HashMap<String, TokenType> keywords = Dict.make(
            new Pair<>("while", CONTROL_WHILE),
            new Pair<>("catch", CONTROL_CATCH),
            new Pair<>("else", CONTROL_ELSE),
            new Pair<>("through", CONTROL_THROUGH),
            new Pair<>("elseif", CONTROL_ELSEIF),
            new Pair<>("stop when", CONTROL_STOP_WHEN),
            new Pair<>("every", CONTROL_EVERY),
            new Pair<>("if", CONTROL_IF),
            new Pair<>("loop", CONTROL_LOOP),
            new Pair<>("when", CONTROL_WHEN),
            new Pair<>("try", CONTROL_TRY),
            new Pair<>("on", CONTROL_ON),
            new Pair<>("for", CONTROL_FOR),

            new Pair<>("bool", TYPE_BOOL),
            new Pair<>("class", TYPE_CLASS),
            new Pair<>("container", TYPE_CONTAINER),
            new Pair<>("func", TYPE_FUNC),
            new Pair<>("list", TYPE_LIST),
            new Pair<>("function", TYPE_FUNCTION),
            new Pair<>("method", TYPE_METHOD),
            new Pair<>("num", TYPE_NUM),
            new Pair<>("object", TYPE_OBJECT),
            new Pair<>("string", TYPE_STRING),
            new Pair<>("void", TYPE_VOID),

            new Pair<>("false", LITERAL_FALSE),
            new Pair<>("true", LITERAL_TRUE),
            new Pair<>("null", LITERAL_NULL),

            new Pair<>("async", ASYNC),

            new Pair<>("as", AS),
            new Pair<>("in", IN),

            new Pair<>("anonymous", ANONYMOUS),
            new Pair<>("override", OVERRIDE),
            new Pair<>("constructor", CONSTRUCTOR),
            new Pair<>("gets", GETS),
            new Pair<>("sets", SETS),

            new Pair<>("break", INSTRUCTION_BREAK),
            new Pair<>("breakpoint", INSTRUCTION_BREAKPOINT),
            new Pair<>("continue", INSTRUCTION_CONTINUE),

            new Pair<>("assert", EFFECT_ASSERT),
            new Pair<>("import", EFFECT_IMPORT),
            new Pair<>("return", EFFECT_RETURN),
            new Pair<>("strike", EFFECT_STRIKE),
            new Pair<>("throw", EFFECT_THROW),
            new Pair<>("use", EFFECT_USE),
            new Pair<>("using", EFFECT_USE),

            new Pair<>("anyof", MOD_ANYOF),
            new Pair<>("local", MOD_LOCAL),
            new Pair<>("final", MOD_FINAL),
            new Pair<>("require", MOD_REQUIRE),
            new Pair<>("static", MOD_STATIC),

            new Pair<>("has", LCPAR),
            new Pair<>("does", LCPAR),
            new Pair<>("with", LCPAR),
            new Pair<>("do", LCPAR),
            new Pair<>("then", LCPAR),
            new Pair<>("end", RCPAR),

            new Pair<>("not", NOT),
            new Pair<>("negate", MINUS),

            new Pair<>("instanceof", INSTANCEOF),
            new Pair<>("is type of", INSTANCEOF),

            new Pair<>("like", LIKE)

    );

    public static final HashMap<TokenType, TokenType> shortToNormal = Dict.make(
            new Pair<>(SHORT_DIVIDE, DIVIDE),
            new Pair<>(SHORT_MODULO, MODULO),
            new Pair<>(SHORT_INTDIV, INTDIV),
            new Pair<>(SHORT_POWER, POWER),
            new Pair<>(SHORT_MINUS, MINUS),
            new Pair<>(SHORT_MULTIPLY, MULTIPLY),
            new Pair<>(SHORT_PLUS, PLUS),
            new Pair<>(SHORT_SHIFT_LEFT, SHIFT_LEFT),
            new Pair<>(SHORT_SHIFT_RIGHT, SHIFT_RIGHT)
    );

}
