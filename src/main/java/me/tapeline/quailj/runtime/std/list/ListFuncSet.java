package me.tapeline.quailj.runtime.std.list;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ListFuncSet extends QBuiltinFunc {

    public ListFuncSet(Runtime runtime) {
        super(
                "set",
                Arrays.asList(
                        new FuncArgument(
                                "list",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_LIST)),
                                false
                        ),
                        new FuncArgument(
                                "index",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_NUM)),
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
        args.get("list").listValue().set(((int) args.get("index").numValue()), args.get("obj"));
        return QObject.Val();
    }

}
