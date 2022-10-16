package me.tapeline.quailj.parsing.nodes.modifiers;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.typing.modifiers.VariableModifier;

import java.util.List;

public class ModifierSequence extends Node {

    public List<VariableModifier> modifiers;

    public ModifierSequence(Token token, List<VariableModifier> modifiers) {
        super(token);
        this.modifiers = modifiers;
    }

}
