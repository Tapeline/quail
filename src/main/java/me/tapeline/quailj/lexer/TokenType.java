package me.tapeline.quailj.lexer;

import java.util.Arrays;
import java.util.List;

public enum TokenType {
    LITERALSTRING("LITERALSTRING", "\\\"[^\\\"]*\\\""),
    LITERALNUM("LITERALNUM", "-?\\d+(\\.\\d+)?"),
    LITERALNULL("LITERALNULL", "(null)[\\s]"),
    LITERALBOOL("LITERALBOOL", "(true|false)[\\s]"),
    COMMA("COMMA", ","),
    TYPE("TYPE", "(func|string|bool|num|container|list|metacontainer|object" +
            "|function|method|staticmethod|object\\<"+
            "|class|anonymous|require|void|anyof|local|final|static)[\\s\\W]"),
    BLOCK("BLOCK", "(do|does|end|then|has|with)[\\s\\W]"),
    KEYWORD("KEYWORD", "(as|through|if|elseif|else|try|catch|while|loop|stop when|every|on|when|override)[\\s\\W]"),
    BINARYOPERATOR("BINARYOPERATOR",
            "(\\:\\+|\\^|\\/\\/|\\+|-|\\*|\\/|\\%|\\:|!=|<-|==|=|<=|>=|>|<|\\.|')"),
    SHORTBINARYOPERATOR("BINARYOPERATOR",
            "(\\^=|\\/\\/=|\\+=|-=|\\*=|\\/=|\\%=)"),
    PILLAR("PILLAR", "\\|"),
    WORDBINARYOPERATOR("BINARYOPERATOR",
            "(is type of|instanceof|and|or|in|is same type as|of|filter|&&|\\|\\|" +
                    "in power of|plus|minus|divided by|multiplied by|is greater than|is less than|is greater or equal to|" +
                    "is less or equal to|is|step|should have|should be|should now be|should now be set|"+
                    "should be set|'s)[\\s\\W]"),
    UNARYOPERATOR("UNARYOPERATOR", "(!|\\&|\\*|##)"),
    WORDUNARYOPERATOR("UNARYOPERATOR", "(not|negate|notnull|exists)[\\s]"),
    EFFECT("EFFECT", "(assert|use|throw|using|deploy|strike|return)[\\s]"),
    INSTRUCTION("INSTRUCTION", "(breakpoint|break|continue|memory)[\\s\\W]"),
    CONSUME("CONSUME", "\\.\\.\\."),
    LAMBDAARROW("LAMBDAARROW", "->"),
    ID("ID", "[a-zA-Z_\\@]+((\\d*[a-zA-Z_\\@]*)*)"),
    LPAR("LPAR", "(\\()"),
    RPAR("RPAR", "(\\))"),
    LSPAR("LSPAR", "(\\[)"),
    RSPAR("RSPAR", "(\\])"),
    LCPAR("LCPAR", "(\\{)"),
    RCPAR("RCPAR", "(\\})"),
    COMMENT("COMMENT", "#.*"),
    WHITESPACE("WHITESPACE", "\\s+");

    public static final List<TokenType> tokenTypeList = Arrays.asList(
            LITERALSTRING, LITERALNUM, LITERALNULL, LITERALBOOL, COMMA, CONSUME, PILLAR,
            LAMBDAARROW, TYPE, BLOCK, KEYWORD, SHORTBINARYOPERATOR, BINARYOPERATOR,
            WORDBINARYOPERATOR, UNARYOPERATOR, WORDUNARYOPERATOR, EFFECT, INSTRUCTION,
            ID, LPAR, RPAR, LSPAR, RSPAR, LCPAR, RCPAR, COMMENT, WHITESPACE);
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
