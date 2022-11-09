package me.tapeline.quailj.typing.modifiers;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;

public class RequireModifier extends VariableModifier {

    public Type type = Type.MOD_REQ;

    public boolean matches(Runtime r, QObject object) {
        return !object.isNull();
    }

    public String toString() {
        return "require";
    }
}
