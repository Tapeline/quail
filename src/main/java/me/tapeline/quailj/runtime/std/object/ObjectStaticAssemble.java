package me.tapeline.quailj.runtime.std.object;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.*;

public class ObjectStaticAssemble extends QBuiltinFunc {

    public ObjectStaticAssemble(Runtime runtime) {
        super(
                "assemble",
                Arrays.asList(
                        new FuncArgument(
                                "keys",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_LIST)),
                                false
                        ),
                        new FuncArgument(
                                "values",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_LIST)),
                                false
                        )
                ),
                runtime,
                runtime.memory,
                true
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker {
        HashMap<String, QObject> obj = new HashMap<>();
        List<QObject> keys = args.get("keys").listValue();
        List<QObject> values = args.get("values").listValue();
        int size;
        if (keys.size() != values.size())
            Runtime.error("Cannot assemble container. Number of keys isn't equal to number of values");
        size = keys.size();
        for (int i = 0; i < size; i++)
            obj.put(keys.get(i).toString(), values.get(i));
        QObject container = QObject.Val(obj);
        container.setObjectMetadata(Runtime.superObject);
        container.setPrototypeFlag(false);
        container.isDict = true;
        return container;
    }

}
