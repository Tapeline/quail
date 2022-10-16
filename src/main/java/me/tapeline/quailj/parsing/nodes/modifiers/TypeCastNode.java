package me.tapeline.quailj.parsing.nodes.modifiers;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;

public class TypeCastNode extends Node {

    public TokenType castedType;
    public Node castedClass;
    public Node value;

    public TypeCastNode(Token token, TokenType castedType, Node castedClass, Node value) {
        super(token);
        this.castedType = castedType;
        this.castedClass = castedClass;
        this.value = value;
    }

    public TypeCastNode(Token token, TokenType castedType, Node value) {
        super(token);
        this.castedType = castedType;
        this.castedClass = null;
        this.value = value;
    }

}
