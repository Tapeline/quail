package me.tapeline.quailj.typing.modifiers;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;

public abstract class VariableModifier {

    public Type type;

    public enum Type {
        MOD_REQ, MOD_TYP, MOD_ANYOF, MOD_LOCAL, MOD_FINAL, MOD_STATIC, MOD_METHOD
    }

    public abstract boolean matches(Runtime runtime, QObject object);

}

