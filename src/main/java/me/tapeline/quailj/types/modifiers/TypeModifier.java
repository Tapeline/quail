package me.tapeline.quailj.types.modifiers;

import me.tapeline.quailj.parser.nodes.Node;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.ContainerType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;

public class TypeModifier extends VariableModifier {

    public Type type = Type.MOD_TYP;
    public Class requiredType;

    public TypeModifier(Class clazz) {
        requiredType = clazz;
    }


    public Node objectClass;

    @Override
    public boolean matches(Runtime r, QType q) {
        try {
            if (objectClass != null) {
                if (q instanceof ContainerType)
                    return ((ContainerType) q).isInstance(r,
                            r.run(objectClass, r.scope).toString());
            }
            return requiredType.isInstance(q);
        } catch (RuntimeStriker ignored) {}
        return false;
    }

    @Override
    public String toString() {
        return objectClass != null? "object<" + objectClass.toString() + ">" :
                requiredType.getSimpleName();
    }
}
