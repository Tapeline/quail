package me.tapeline.quailj.tokenizetools.tokens;

import java.util.Arrays;
import java.util.List;

public enum TokenType {
    LITERALSTRING("LITERALSTRING", "\\\"[^\\\"]*\\\""),
    LITERALNUM("LITERALNUM", "-?\\d+(\\.\\d+)?"),
    LITERALNULL("LITERALNULL", "(null|nothing)[\\s]"),
    LITERALBOOL("LITERALBOOL", "(true|false)[\\s]"),
    TYPE("TYPE", "(func|string|bool|num|container|list|metacontainer|object|function|method|staticmethod|class)[\\s\\W]"),
    MODIFIER("MODIFIER", "(local|final|anonymous)[\\s\\W]"),
    BLOCK("BLOCK", "(do|does|end|then|has|with)[\\s\\W]"),
    COMMA("COMMA", ","),
    KEYWORD("KEYWORD", "(as|in|through|if|elseif|else|try|catch|while|loop|stop when|every|on|when|override)[\\s\\W]"),
    BINARYOPERATOR("BINARYOPERATOR",
              "(\\^|\\/\\/|\\+|-|\\*|\\/|\\%|==|"+
                  "<=|>=|>|<|\\.\\.\\.|\\.|'s|')"),
    WORDBINARYOPERATOR("BINARYOPERATOR",
            "(is type of|instanceof|and|or|at|in|is same type as|of|" +
            "in power of|plus|minus|divided by|multiplied by|is greater than|is less than|is greater or equal to|" +
            "is less or equal to|is|step)[\\s\\W]"),
    ASSIGNOPERATOR("ASSIGNOPERATOR", "(=|should have|should be|should now be|should now be set|should be set)"),
    UNARYOPERATOR("UNARYOPERATOR",
            "(not|negate|notnull|exists)[\\s]"),
    STATEMENT("STATEMENT", "(destroy|assert|use|block|throw|using|deploy|return)[\\s]"),
    INSTRUCTION("INSTRUCTION", "(milestone|breakpoint|break|continue|memory)[\\s\\W]"),
    ID("ID", "[a-zA-Z_\\@]+((\\d*[a-zA-Z_\\@]*)*)"),
    LPAR("LPAR", "(\\()"),
    RPAR("RPAR", "(\\))"),
    LSPAR("LSPAR", "(\\[)"),
    RSPAR("RSPAR", "(\\])"),
    LCPAR("LCPAR", "(\\{)"),
    RCPAR("RCPAR", "(\\})"),
    SEMICOLON("SEMICOLON", "(\\;)"),
    COMMENT("COMMENT", "#.*"),
    WHITESPACE("WHITESPACE", "\\s+");

    public static final List<TokenType> tokenTypeList = Arrays.asList(
            LITERALSTRING, LITERALNUM, LITERALNULL, BINARYOPERATOR, WORDBINARYOPERATOR, LPAR, RPAR, LSPAR,
            RSPAR, LCPAR, RCPAR, ASSIGNOPERATOR, COMMA, LITERALBOOL, TYPE, MODIFIER, BLOCK, KEYWORD,
            UNARYOPERATOR, STATEMENT, INSTRUCTION, ID, COMMENT, SEMICOLON, WHITESPACE);
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
