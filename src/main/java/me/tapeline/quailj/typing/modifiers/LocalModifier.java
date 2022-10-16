package me.tapeline.quailj.typing.modifiers;

public class LocalModifier extends VariableModifier {

    public Type type = Type.MOD_LOCAL;

    public boolean matches(Runtime r, Object object) {
        return true;
    }

    public String toString() {
        return "local";
    }
}
