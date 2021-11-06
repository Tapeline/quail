package me.tapeline.quarkj.tokenizetools.tokens;

import java.util.Arrays;
import java.util.List;

public enum TokenType {
    LITERALSTRING("LITERALSTRING", "\\\"[^\\\"]*\\\""),
    LITERALNUM("LITERALNUM", "-?\\d+(\\.\\d+)?"),
    LITERALBOOL("LITERALBOOL", "(true|false)[\\s^\\w]"),
    TYPE("TYPE", "(func|string|bool|num)[\\s\\W]"),
    MODIFIER("MODIFIER", "(local|final)[\\s\\W]"),
    BLOCK("BLOCK", "(do|does|end|is)[\\s\\W]"),
    COMMA("COMMA", ","),
    KEYWORD("KEYWORD", "(as|in|nothing|through|if|elseif|else|try|catch|while|loop|stop when)[\\s\\W]"),
    BINARYOPERATOR("BINARYOPERATOR", "(\\^|\\/\\/|\\+|-|\\*|\\/|\\%|and|or|==|<=|>=|>|<|\\.\\.\\.|\\.\\.|\\.|\\:\\:)"),
    ASSIGNOPERATOR("ASSIGNOPERATOR", "(=)"),
    UNARYOPERATOR("UNARYOPERATOR",
            "(not|out|input|numinput|boolinput|exists|tostring|tonum|tobool|destroy|put|assert|return|block|len|throw|notnull)[\\s\\W^\\(]"),
    INSTRUCTION("INSTRUCTION", "(milestone|breakpoint|break|continue|memory)[\\s\\W]"),
    ID("ID", "[a-zA-Z_]+\\d{0,}"),
    LPAR("LPAR", "(\\()"),
    RPAR("RPAR", "(\\))"),
    COMMENT("COMMENT", "#.*"),
    WHITESPACE("WHITESPACE", "\\s+");

    public static List<TokenType> tokenTypeList = Arrays.asList(
            LITERALSTRING, LITERALNUM, BINARYOPERATOR, LPAR, RPAR, ASSIGNOPERATOR, COMMA,
            LITERALBOOL, TYPE, MODIFIER, BLOCK, KEYWORD, UNARYOPERATOR, INSTRUCTION, ID, COMMENT, WHITESPACE);
    private final String s;
    public final String regex;

    TokenType(String s, String r) {
        this.s = s;
        this.regex = r;
    }
    
    @Override
    public String toString() {
        return s;
    }
}
