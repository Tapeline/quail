package me.tapeline.quailj.runtime.std.list;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class ListFuncAdd extends QBuiltinFunc {

    public ListFuncAdd(Runtime runtime) {
        super(
                "add",
                Arrays.asList(
                        new FuncArgument(
                                "list",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_LIST)),
                                false
                        ),
                        new FuncArgument(
                                "arg1",
                                new ArrayList<>(),
                                false
                        ),
                        new FuncArgument(
                                "arg2",
                                new ArrayList<>(),
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
        if (args.get("arg2") == null || args.get("arg2").isNull())
            args.get("list").listValue().add(args.get("arg1"));
        else
            args.get("list").listValue().add(((int) args.get("arg1").numValue()), args.get("arg2"));
        return QObject.Val();
    }

}
