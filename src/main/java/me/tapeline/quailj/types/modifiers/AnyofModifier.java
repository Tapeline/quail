package me.tapeline.quailj.types.modifiers;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.QType;

import java.util.List;

public class AnyofModifier extends VariableModifier {

    public Type type = Type.MOD_ANYOF;
    public TypeModifier[] acceptedTypes;

    public AnyofModifier(TypeModifier... tms) {
        acceptedTypes = tms;
    }

    public AnyofModifier(List<TypeModifier> tms) {
        acceptedTypes = tms.toArray(new TypeModifier[1]);
    }


    @Override
    public boolean matches(Runtime r, QType q) {
        for (TypeModifier t : acceptedTypes)
            if (t.matches(r, q)) return true;
        return false;
    }

    public String toString() {
        String s = "";
        for (TypeModifier m : acceptedTypes)
            s += m.toString() + " | ";
        return s.length() == 0? "" : s.substring(0, s.length() - 3);
    }
}
