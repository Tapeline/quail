package me.tapeline.quailj.runtime.std.standart.common;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.*;

public class FuncEnumerate extends QBuiltinFunc {

    public FuncEnumerate(Runtime runtime) {
        super(
                "enumerate",
                Collections.singletonList(
                        new FuncArgument(
                                "collection",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_LIST)),
                                false
                        )
                ),
                runtime,
                runtime.memory,
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) {
        List<QObject> values = args.get("collection").listValue();
        int size = values.size();
        List<QObject> enumerated = new ArrayList<>();
        for (int i = 0; i < size; i++)
            enumerated.add(QObject.Val(Arrays.asList(QObject.Val(i), values.get(i))));
        return QObject.Val(enumerated);
    }

}
