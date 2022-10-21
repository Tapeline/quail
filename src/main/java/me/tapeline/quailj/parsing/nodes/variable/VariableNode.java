package me.tapeline.quailj.parsing.nodes.variable;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.VariableModifier;
import me.tapeline.quailj.typing.objects.QObject;

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

    public boolean matchesRequirements(Runtime r, QObject q) {
        for (VariableModifier vm : modifiers)
            if (!vm.matches(r, q))
                return false;
        return true;
    }

    public static boolean match(List<VariableModifier> modifiers, Runtime r, QObject q) {
        for (VariableModifier vm : modifiers)
            if (!vm.matches(r, q))
                return false;
        return true;
    }
}
