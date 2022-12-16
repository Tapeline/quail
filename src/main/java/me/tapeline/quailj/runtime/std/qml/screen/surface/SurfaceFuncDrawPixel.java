package me.tapeline.quailj.runtime.std.qml.screen.surface;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class SurfaceFuncDrawPixel extends QBuiltinFunc {

    public SurfaceFuncDrawPixel(Runtime runtime) {
        super(
                "drawPixel",
                Arrays.asList(
                        new FuncArgument(
                                "surface",
                                new ArrayList<>(),
                                false
                        ),
                        new FuncArgument(
                                "x",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_NUM)),
                                false
                        ),
                        new FuncArgument(
                                "y",
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
    public QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker {
        if (!(args.get("surface") instanceof QMLSurface))
            Runtime.error("Not a surface");
        QMLSurface surface = ((QMLSurface) args.get("surface"));
        surface.graphics.drawRect(
                (int) args.get("x").numValue(),
                (int) args.get("y").numValue(),
                1, 1
        );
        return QObject.Val();
    }

}
