package me.tapeline.quailj.runtime.std.ji;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QJavaAdapter;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.Collections;
import java.util.HashMap;

public class JiFuncBoolean extends QBuiltinFunc {

    public JiFuncBoolean(Runtime runtime) {
        super(
                "boolean",
                Collections.singletonList(
                        new FuncArgument(
                                "n",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_BOOL)),
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
        return new QJavaAdapter<>(runtime, args.get("n").boolValue());
    }

}
