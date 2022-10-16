package me.tapeline.quailj.typing.modifiers;

public abstract class VariableModifier {

    public Type type;

    public enum Type {
        MOD_REQ, MOD_TYP, MOD_ANYOF, MOD_LOCAL, MOD_FINAL, MOD_STATIC
    }

    public abstract boolean matches(Runtime runtime, Object object);

}

