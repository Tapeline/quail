package me.tapeline.quailj.runtime.std.ji;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QJavaAdapter;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.Arrays;
import java.util.HashMap;

public class JiFuncDouble extends QBuiltinFunc {

    public JiFuncDouble(Runtime runtime) {
        super(
                "double",
                Arrays.asList(
                        new FuncArgument(
                                "n",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_NUM)),
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
        return new QJavaAdapter<>(runtime, args.get("n").numValue());
    }

}
