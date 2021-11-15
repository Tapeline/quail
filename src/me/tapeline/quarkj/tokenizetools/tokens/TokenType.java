package me.tapeline.quarkj.tokenizetools.tokens;

import java.util.Arrays;
import java.util.List;

public enum TokenType {
    LITERALSTRING("LITERALSTRING", "\\\"[^\\\"]*\\\""),
    LITERALNUM("LITERALNUM", "-?\\d+(\\.\\d+)?"),
    LITERALBOOL("LITERALBOOL", "(true|false)[\\s]"),
    TYPE("TYPE", "(func|string|bool|num|container|list)[\\s\\W]"),
    MODIFIER("MODIFIER", "(local|final)[\\s\\W]"),
    BLOCK("BLOCK", "(do|does|end|is)[\\s\\W]"),
    COMMA("COMMA", ","),
    KEYWORD("KEYWORD", "(as|in|through|if|elseif|else|try|catch|while|loop|stop when|every)[\\s\\W]"),
    BINARYOPERATOR("BINARYOPERATOR", "(\\^|\\/\\/|\\+|-|\\*|\\/|\\%|and|or|==|<=|>=|>|<|\\.\\.\\.|\\.\\.|\\.)"),
    ASSIGNOPERATOR("ASSIGNOPERATOR", "(=)"),
    UNARYOPERATOR("UNARYOPERATOR",
            "(not|out|input|numinput|boolinput|exists|tostring|tonum|tobool|destroy|put|assert|return|block|throw|notnull|import|file_new|file_read|file_write|file_append|file_exists)[\\s]"),
    INSTRUCTION("INSTRUCTION", "(milestone|breakpoint|break|continue|memory|nothing)[\\s\\W]"),
    ID("ID", "[a-zA-Z_]+\\d{0,}"),
    LPAR("LPAR", "(\\()"),
    RPAR("RPAR", "(\\))"),
    LSPAR("LSPAR", "(\\[)"),
    RSPAR("RSPAR", "(\\])"),
    COMMENT("COMMENT", "#.*"),
    WHITESPACE("WHITESPACE", "\\s+");

    public static List<TokenType> tokenTypeList = Arrays.asList(
            LITERALSTRING, LITERALNUM, BINARYOPERATOR, LPAR, RPAR, LSPAR, RSPAR, ASSIGNOPERATOR, COMMA,
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
