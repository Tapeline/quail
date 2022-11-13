package me.tapeline.quailj.runtime.std.list;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ListFuncPop extends QBuiltinFunc {

    public ListFuncPop(Runtime runtime) {
        super(
                "pop",
                Arrays.asList(
                        new FuncArgument(
                                "list",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_LIST)),
                                false
                        )
                ),
                runtime,
                runtime.memory,
                false
        );
    }

    @Override
    public QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker {
        List<QObject> list = args.get("list").listValue();
        QObject object = list.get(list.size() - 1);
        list.remove(list.size() - 1);
        return object;
    }

}
