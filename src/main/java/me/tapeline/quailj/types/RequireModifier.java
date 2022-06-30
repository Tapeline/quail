package me.tapeline.quailj.types;

import me.tapeline.quailj.runtime.Runtime;

public class RequireModifier extends VariableModifier {

    public Type type = Type.MOD_REQ;

    @Override
    public boolean matches(Runtime r, QType q) {
        return !(q instanceof VoidType) && q != null;
    }
}
