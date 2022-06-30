package me.tapeline.quailj.types;

import me.tapeline.quailj.runtime.Runtime;

public abstract class VariableModifier {

    public Type type;

    public enum Type {
        MOD_REQ, MOD_TYP, MOD_ANYOF, MOD_LOCAL
    }

    public abstract boolean matches(Runtime r, QType q);

}
