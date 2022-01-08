package me.tapeline.quailj.tokenizetools.tokens;

import java.util.Arrays;
import java.util.List;

public enum TokenType {
    LITERALSTRING("LITERALSTRING", "\\\"[^\\\"]*\\\""),
    LITERALNUM("LITERALNUM", "-?\\d+(\\.\\d+)?"),
    LITERALNULL("LITERALNULL", "(null|nothing)[\\s]"),
    LITERALBOOL("LITERALBOOL", "(true|false)[\\s]"),
    TYPE("TYPE", "(func|string|bool|num|container|list|metacontainer|object|function)[\\s\\W]"),
    MODIFIER("MODIFIER", "(local|final)[\\s\\W]"),
    BLOCK("BLOCK", "(do|does|end|then|has)[\\s\\W]"),
    COMMA("COMMA", ","),
    KEYWORD("KEYWORD", "(as|in|through|if|elseif|else|try|catch|while|loop|stop when|every|on|when)[\\s\\W]"),
    BINARYOPERATOR("BINARYOPERATOR", "(\\^|\\/\\/|\\+|-|\\*|\\/|\\%|and|or|==|is same type as|is type of|instanceof|<=|>=|>|<|\\.\\.\\.|\\.|of|'s|'|is)"),
    ASSIGNOPERATOR("ASSIGNOPERATOR", "(=)"),
    UNARYOPERATOR("UNARYOPERATOR",
            "(reference to|not|negate|my|out|input|exists|destroy|put|assert|use|block|throw|notnull|using|deploy|return)[\\s]"),
    INSTRUCTION("INSTRUCTION", "(milestone|breakpoint|break|continue|memory)[\\s\\W]"),
    ID("ID", "[a-zA-Z_\\@]+((\\d*[a-zA-Z_\\@]*)*)"),
    LPAR("LPAR", "(\\()"),
    RPAR("RPAR", "(\\))"),
    LSPAR("LSPAR", "(\\[)"),
    RSPAR("RSPAR", "(\\])"),
    LCPAR("LCPAR", "(\\{)"),
    RCPAR("RCPAR", "(\\})"),
    COLON("COLON", "(\\:)"),
    COMMENT("COMMENT", "#.*"),
    WHITESPACE("WHITESPACE", "\\s+");

    public static List<TokenType> tokenTypeList = Arrays.asList(
            LITERALSTRING, LITERALNUM, LITERALNULL, BINARYOPERATOR, LPAR, RPAR, LSPAR, RSPAR, LCPAR, RCPAR,
            ASSIGNOPERATOR, COMMA, LITERALBOOL, TYPE, MODIFIER, BLOCK, KEYWORD, UNARYOPERATOR, INSTRUCTION,
            ID, COMMENT, WHITESPACE);
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
