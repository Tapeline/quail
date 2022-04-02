package me.tapeline.quailj.lexer;

public class Token {
    public final TokenType t;
    public String c;
    public final int p;

    public Token(TokenType t, String c, int p) {
        this.t = t;
        this.c = c;
        this.p = p;
    }

    @Override
    public String toString() {
        return c;
    }

    public String srepr() {
        return t + "[" + c + "]:" + p;
    }
}
