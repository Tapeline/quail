package me.tapeline.quailj.parsing.nodes.variable;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.typing.modifiers.VariableModifier;

import java.util.List;

public class VariableNode extends Node {

    public String id;
    public boolean isArgConsumer;
    public boolean isKwargConsumer;
    public List<VariableModifier> modifiers;

    public VariableNode(Token token, String id,
                        boolean isArgConsumer,
                        boolean isKwargConsumer,
                        List<VariableModifier> modifiers) {
        super(token);
        this.id = id;
        this.isArgConsumer = isArgConsumer;
        this.isKwargConsumer = isKwargConsumer;
        this.modifiers = modifiers;
    }

}
