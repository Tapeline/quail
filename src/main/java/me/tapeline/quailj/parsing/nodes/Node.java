package me.tapeline.quailj.parsing.nodes;

import me.tapeline.quailj.lexing.Token;

public class Node {

    public int line;
    public int character;
    public int length;
    public long executionTime = -1;
    public long executionStart = -1;

    public Node(Token token) {
        this.line = token.getLine();
        this.character = token.getCharacter();
        this.length = token.getLength();
    }

}
