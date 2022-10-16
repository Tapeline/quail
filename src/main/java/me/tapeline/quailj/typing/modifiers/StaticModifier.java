package me.tapeline.quailj.typing.modifiers;

public class StaticModifier extends VariableModifier {

    public Type type = Type.MOD_STATIC;

    public boolean matches(Runtime r, Object object) {
        return true;
    }

    public String toString() {
        return "static";
    }
}
