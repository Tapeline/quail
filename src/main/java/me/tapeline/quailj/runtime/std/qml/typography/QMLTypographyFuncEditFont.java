package me.tapeline.quailj.runtime.std.qml.typography;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QJavaAdapter;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class QMLTypographyFuncEditFont extends QBuiltinFunc {

    public QMLTypographyFuncEditFont(Runtime runtime) {
        super(
                "createFont",
                Arrays.asList(
                        new FuncArgument(
                                "font",
                                new ArrayList<>(),
                                false
                        ),
                        new FuncArgument(
                                "style",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_NUM)),
                                false
                        ),
                        new FuncArgument(
                                "size",
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
        if (!(args.get("font") instanceof QJavaAdapter) ||
            !(((QJavaAdapter<?>) args.get("font")).object instanceof Font))
            Runtime.error("Not a font");
        return new QJavaAdapter<>(runtime, ((Font) ((QJavaAdapter<?>) args.get("font")).object).deriveFont(
                (int) args.get("style").numValue(),
                (float) args.get("size").numValue()
        ));
    }

}
