package me.tapeline.quailj.parsing.nodes.block;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;

public class JavaEmbedNode extends Node {

    public String code;

    public JavaEmbedNode(Token token) {
        super(token);
        this.code = token.getLexeme();
    }

}
