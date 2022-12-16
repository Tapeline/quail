package me.tapeline.quailj.runtime.std.list;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.Collections;
import java.util.HashMap;

public class ListFuncClear extends QBuiltinFunc {

    public ListFuncClear(Runtime runtime) {
        super(
                "clear",
                Collections.singletonList(
                        new FuncArgument(
                                "list",
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
        args.get("list").listValue().clear();
        return QObject.Val();
    }

}
