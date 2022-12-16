package me.tapeline.quailj.runtime.std.qml.typography;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QJavaAdapter;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class QMLTypographyFuncCreateFont extends QBuiltinFunc {

    public QMLTypographyFuncCreateFont(Runtime runtime) {
        super(
                "createFont",
                Arrays.asList(
                        new FuncArgument(
                                "font",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_STRING)),
                                false
                        ),
                        new FuncArgument(
                                "style",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_NUM)),
                                false
                        ),
                        new FuncArgument(
                                "size",
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
        return new QJavaAdapter<>(runtime, new Font(
                args.get("font").toString(),
                (int) args.get("style").numValue(),
                (int) args.get("size").numValue()
        ));
    }

}
