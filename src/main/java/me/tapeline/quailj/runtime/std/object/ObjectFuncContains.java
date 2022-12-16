package me.tapeline.quailj.runtime.std.object;

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

public class ObjectFuncContains extends QBuiltinFunc {

    public ObjectFuncContains(Runtime runtime) {
        super(
                "contains",
                Arrays.asList(
                        new FuncArgument(
                                "obj",
                                new ArrayList<>(),
                                false
                        ),
                        new FuncArgument(
                                "key",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_STRING)),
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
        return QObject.Val(args.get("obj").getTable().containsKey(args.get("key").toString()));
    }

}
