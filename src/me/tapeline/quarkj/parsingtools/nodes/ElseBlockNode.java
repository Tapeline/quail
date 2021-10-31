package me.tapeline.quarkj.parsingtools.nodes;

import me.tapeline.quarkj.tokenizetools.tokens.Token;
import me.tapeline.quarkj.tokenizetools.tokens.TokenType;

public class ElseBlockNode extends Node {

    public final BlockNode nodes;
    public final Token token = new Token(TokenType.WHITESPACE, "", 0);

    public ElseBlockNode(BlockNode block) {
        this.nodes = block;
    }

    @Override
    public String toString() {
        return "ElseNode=" + nodes.toString() + "\n";
    }

}
