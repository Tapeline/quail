package me.tapeline.quailj.runtime.std.list;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.*;

public class ListFuncFind extends QBuiltinFunc {

    public ListFuncFind(Runtime runtime) {
        super(
                "count",
                Arrays.asList(
                        new FuncArgument(
                                "list",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_LIST)),
                                false
                        ),
                        new FuncArgument(
                                "obj",
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
    public QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker {
        List<QObject> list = args.get("list").listValue();
        int size = list.size();
        for (int i = 0; i < size; i++)
            if (list.get(i).equalsObject(runtime, args.get("obj")).isTrue())
                return QObject.Val(i);
        return QObject.Val(-1);
    }

}
