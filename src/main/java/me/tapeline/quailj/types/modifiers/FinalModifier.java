package me.tapeline.quailj.types.modifiers;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.QType;

public class FinalModifier extends VariableModifier {

    public Type type = Type.MOD_FINAL;
    public boolean flag = false;

    @Override
    public boolean matches(Runtime r, QType q) {
        return false;
    }

    public String toString() {
        return "final";
    }
}
