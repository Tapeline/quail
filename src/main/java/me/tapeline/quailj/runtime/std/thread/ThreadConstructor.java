package me.tapeline.quailj.runtime.std.thread;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.RequireModifier;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QFunc;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class ThreadConstructor extends QBuiltinFunc {

    public ThreadConstructor(Runtime runtime) {
        super(
                "_constructor",
                Arrays.asList(
                        new FuncArgument(
                                "this",
                                new ArrayList<>(),
                                false
                        ),
                        new FuncArgument(
                                "func",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_FUNC)),
                                false
                        ),
                        new FuncArgument(
                                "args",
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
        return new QThread(runtime, ((QFunc) args.get("func")), args.get("args").listValue());
    }

}
