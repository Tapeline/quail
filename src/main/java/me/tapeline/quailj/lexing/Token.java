package me.tapeline.quailj.lexing;

public class Token {
    private TokenType type;
    private String lexeme;
    private int line;

    private int character;
    private int length;

    public Token(TokenType type, String lexeme, int line, int character, int length) {
        this.type = type;
        this.lexeme = lexeme;
        this.line = line;
        this.character = character;
        this.length = length;
    }

    public TokenType getType() {
        return type;
    }

    public String getLexeme() {
        return lexeme;
    }

    public int getLine() {
        return line;
    }

    public int getCharacter() {
        return character;
    }

    public int getLength() {
        return length;
    }

    public String toString() {
        return type + " " + lexeme;
    }
}