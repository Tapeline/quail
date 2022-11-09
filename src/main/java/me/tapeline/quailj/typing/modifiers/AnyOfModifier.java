package me.tapeline.quailj.typing.modifiers;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;

import java.util.ArrayList;
import java.util.List;

public class AnyOfModifier extends VariableModifier {

    public Type type = Type.MOD_ANYOF;
    public List<VariableModifier> conditions = new ArrayList<>();

    public AnyOfModifier(List<VariableModifier> conditions) {
        this.conditions = conditions;
    }

    public boolean matches(Runtime r, QObject object) {
        for (VariableModifier condition : conditions)
            if (condition.matches(r, object))
                return true;
        return false;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("anyof ");
        for (VariableModifier modifier : conditions)
            sb.append(" | ").append(modifier.toString());
        return sb.toString();
    }
}
