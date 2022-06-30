package me.tapeline.quailj.types;

import me.tapeline.quailj.runtime.Runtime;

public class LocalModifier extends VariableModifier {

    public Type type = Type.MOD_LOCAL;

    @Override
    public boolean matches(Runtime r, QType q) {
        return true;
    }
}
