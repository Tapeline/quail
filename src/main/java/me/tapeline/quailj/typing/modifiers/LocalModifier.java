package me.tapeline.quailj.typing.modifiers;

import me.tapeline.quailj.runtime.Runtime;

public class LocalModifier extends VariableModifier {

    public Type type = Type.MOD_LOCAL;

    public boolean matches(Runtime r, Object object) {
        return true;
    }

    public String toString() {
        return "local";
    }
}
