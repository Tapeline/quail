package me.tapeline.quailj.types.modifiers;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.QType;

public class LocalModifier extends VariableModifier {

    public Type type = Type.MOD_LOCAL;

    @Override
    public boolean matches(Runtime r, QType q) {
        return true;
    }

    public String toString() {
        return "local";
    }
}
