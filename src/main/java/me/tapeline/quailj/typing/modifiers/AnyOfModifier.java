package me.tapeline.quailj.typing.modifiers;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.parsing.nodes.Node;

import java.util.ArrayList;
import java.util.List;

public class AnyOfModifier extends VariableModifier {

    public Type type = Type.MOD_ANYOF;
    public List<VariableModifier> conditions = new ArrayList<>();

    public AnyOfModifier(List<VariableModifier> conditions) {
        this.conditions = conditions;
    }

    public boolean matches(Runtime r, Object object) {
        for (VariableModifier condition : conditions)
            if (condition.matches(r, object))
                return true;
        return false;
    }

    public String toString() {
        return "type";
    }
}
