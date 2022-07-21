package me.tapeline.quailj.types.modifiers;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.VoidType;

public class RequireModifier extends VariableModifier {

    public Type type = Type.MOD_REQ;

    @Override
    public boolean matches(Runtime r, QType q) {
        return !(q instanceof VoidType) && q != null;
    }

    public String toString() {
        return "require";
    }
}
