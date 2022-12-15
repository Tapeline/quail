package me.tapeline.quailj.runtime.std.qml.screen.surface;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class SurfaceFuncSetColorHSB extends QBuiltinFunc {

    public SurfaceFuncSetColorHSB(Runtime runtime) {
        super(
                "setColorHSB",
                Arrays.asList(
                        new FuncArgument(
                                "surface",
                                new ArrayList<>(),
                                false
                        ),
                        new FuncArgument(
                                "h",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_NUM)),
                                false
                        ),
                        new FuncArgument(
                                "s",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_NUM)),
                                false
                        ),
                        new FuncArgument(
                                "b",
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
        surface.graphics.setColor(
                Color.getHSBColor(
                        (float) args.get("h").numValue(),
                        (float) args.get("s").numValue(),
                        (float) args.get("b").numValue()
                )
        );
        return QObject.Val();
    }

}
