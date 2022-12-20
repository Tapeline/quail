package me.tapeline.quailj.typing.modifiers;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;

public class MethodModifier extends VariableModifier {

    public Type type = Type.MOD_METHOD;

    public boolean matches(Runtime r, QObject object) {
        return true;
    }

    public String toString() {
        return "method";
    }
}
