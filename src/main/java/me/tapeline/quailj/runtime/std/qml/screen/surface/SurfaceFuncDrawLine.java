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
import java.util.HashMap;

public class SurfaceFuncDrawLine extends QBuiltinFunc {

    public SurfaceFuncDrawLine(Runtime runtime) {
        super(
                "drawLine",
                Arrays.asList(
                        new FuncArgument(
                                "surface",
                                new ArrayList<>(),
                                false
                        ),
                        new FuncArgument(
                                "x",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_NUM)),
                                false
                        ),
                        new FuncArgument(
                                "y",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_NUM)),
                                false
                        ),
                        new FuncArgument(
                                "x2",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_NUM)),
                                false
                        ),
                        new FuncArgument(
                                "y2",
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
        if (!(args.get("surface") instanceof QMLSurface))
            Runtime.error("Not a surface");
        QMLSurface surface = ((QMLSurface) args.get("surface"));
        surface.graphics.drawLine(
                (int) args.get("x").numValue(),
                (int) args.get("y").numValue(),
                (int) args.get("x2").numValue(),
                (int) args.get("y2").numValue()
        );
        return QObject.Val();
    }

}
