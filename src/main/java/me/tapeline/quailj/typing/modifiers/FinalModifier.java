package me.tapeline.quailj.typing.modifiers;

public class FinalModifier extends VariableModifier {

    public Type type = Type.MOD_LOCAL;

    private boolean hadAssignment = false;

    public boolean matches(Runtime r, Object object) {
        if (!hadAssignment)
            hadAssignment = true;
        else
            return false;
        return true;
    }

    public String toString() {
        return "local";
    }
}
