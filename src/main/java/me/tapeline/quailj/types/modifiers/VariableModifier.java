package me.tapeline.quailj.types.modifiers;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.QType;

public abstract class VariableModifier {

    public Type type;

    public enum Type {
        MOD_REQ, MOD_TYP, MOD_ANYOF, MOD_LOCAL, MOD_FINAL, MOD_STATIC
    }

    public abstract boolean matches(Runtime r, QType q);

}
