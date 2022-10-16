package me.tapeline.quailj.typing.modifiers;

public class RequireModifier extends VariableModifier {

    public Type type = Type.MOD_REQ;

    public boolean matches(Runtime r, Object object) {
        return true;
    }

    public String toString() {
        return "require";
    }
}
