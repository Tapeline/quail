package me.tapeline.quailj.types.modifiers;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.QType;

public class StaticModifier extends VariableModifier {

    public Type type = Type.MOD_STATIC;

    @Override
    public boolean matches(Runtime r, QType q) {
        return true;
    }

    public String toString() {
        return "static";
    }
}
