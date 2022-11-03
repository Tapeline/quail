package me.tapeline.quailj.parsing.nodes.effect;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;

public class InstructionNode extends Node {

    public TokenType instruction;

    public InstructionNode(Token token, TokenType instruction) {
        super(token);
        this.instruction = instruction;
    }

    public InstructionNode(Token token) {
        super(token);
        this.instruction = token.getType();
    }

}
