package me.tapeline.quailj.runtime.std.standart.common;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class FuncAny extends QBuiltinFunc {

    public FuncAny(Runtime runtime) {
        super(
                "any",
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
        for (int i = 0; i < size; i++)
            if (values.get(i).isTrue())
                return QObject.Val(true);
        return QObject.Val(false);
    }

}
