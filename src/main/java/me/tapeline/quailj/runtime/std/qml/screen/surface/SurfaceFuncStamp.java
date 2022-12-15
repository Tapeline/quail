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

public class SurfaceFuncStamp extends QBuiltinFunc {

    public SurfaceFuncStamp(Runtime runtime) {
        super(
                "stamp",
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
                                "surface2",
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
        if (!(args.get("surface") instanceof QMLSurface))
            Runtime.error("Not a surface");
        QMLSurface surface = ((QMLSurface) args.get("surface"));
        if (!(args.get("surface2") instanceof QMLSurface))
            Runtime.error("Stamping object is not a surface");
        QMLSurface surface2 = ((QMLSurface) args.get("surface2"));
        surface.graphics.drawImage(
                surface2.image,
                (int) args.get("x").numValue(),
                (int) args.get("y").numValue(),
                null
        );
        return QObject.Val();
    }

}
