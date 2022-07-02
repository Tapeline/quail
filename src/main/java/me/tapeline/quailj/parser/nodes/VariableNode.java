package me.tapeline.quailj.parser.nodes;

import me.tapeline.quailj.lexer.Token;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.VariableModifier;

import java.util.ArrayList;
import java.util.List;

public class VariableNode extends Node {

    public final Token token;
    public boolean isConsumer = false;
    public List<VariableModifier> modifiers = new ArrayList<>();

    public VariableNode(Token token) {
        this.token = token;
        this.codePos = token.p;
    }

    public boolean matchesRequirements(Runtime r, QType q) {
        for (VariableModifier vm : modifiers)
            if (!vm.matches(r, q))
                return false;
        return true;
    }

    public static boolean match(List<VariableModifier> modifiers, Runtime r, QType q) {
        for (VariableModifier vm : modifiers)
            if (!vm.matches(r, q))
                return false;
        return true;
    }

    @Override
    public String toString() {
        String s = "";
        for (VariableModifier vm : modifiers)
            s += vm.toString() + " ";
        return s + token.c + (isConsumer? "..." : "");
    }
}
