package me.tapeline.quailj.typing.objects.funcutils;

import me.tapeline.quailj.lexing.Token;
import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;
import me.tapeline.quailj.parsing.nodes.literals.LiteralNull;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.RequireModifier;
import me.tapeline.quailj.typing.modifiers.VariableModifier;
import me.tapeline.quailj.typing.objects.QObject;

import java.util.List;

public class FuncArgument {

    public static Node defaultNull = new LiteralNull(
            new Token(TokenType.LITERAL_NULL, "null", 1, 0, 0));

    public String name;
    public Node defaultValue = defaultNull;
    public List<VariableModifier> modifiers;
    public boolean isArgsConsumer;
    public boolean couldBeNull = true;

    public FuncArgument(String name,
                        Node defaultValue,
                        List<VariableModifier> modifiers,
                        boolean isArgsConsumer) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.modifiers = modifiers;
        this.isArgsConsumer = isArgsConsumer;
        for (VariableModifier modifier : modifiers)
            if (modifier instanceof RequireModifier) {
                couldBeNull = false;
                break;
            }
    }

    public FuncArgument(String name,
                        List<VariableModifier> modifiers,
                        boolean isArgsConsumer) {
        this.name = name;
        this.modifiers = modifiers;
        this.isArgsConsumer = isArgsConsumer;
        for (VariableModifier modifier : modifiers)
            if (modifier instanceof RequireModifier) {
                couldBeNull = false;
                break;
            }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (VariableModifier modifier : modifiers)
            sb.append(modifier.toString()).append(" ");
        return sb.append(name).toString();
    }
    public boolean matchesRequirements(Runtime r, QObject q) {
        for (VariableModifier vm : modifiers)
            if (!vm.matches(r, q))
                return false;
        return true;
    }
}
