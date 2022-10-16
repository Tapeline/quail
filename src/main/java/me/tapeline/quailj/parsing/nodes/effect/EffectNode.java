package me.tapeline.quailj.parsing.nodes.effect;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;

public class EffectNode extends Node {

    public TokenType effect;
    public Node value;

    public EffectNode(Token token, TokenType effect, Node value) {
        super(token);
        this.effect = effect;
        this.value = value;
    }
    
}
