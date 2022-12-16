package me.tapeline.quailj.runtime.std.list;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class ListFuncGet extends QBuiltinFunc {

    public ListFuncGet(Runtime runtime) {
        super(
                "get",
                Arrays.asList(
                        new FuncArgument(
                                "list",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_LIST)),
                                false
                        ),
                        new FuncArgument(
                                "index",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_NUM)),
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
        return args.get("list").listValue().get((int) args.get("index").numValue());
    }

}
