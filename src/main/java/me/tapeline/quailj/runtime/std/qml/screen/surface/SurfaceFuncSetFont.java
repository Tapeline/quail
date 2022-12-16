package me.tapeline.quailj.runtime.std.qml.screen.surface;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QJavaAdapter;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.QString;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class SurfaceFuncSetFont extends QBuiltinFunc {

    public SurfaceFuncSetFont(Runtime runtime) {
        super(
                "setFont",
                Arrays.asList(
                        new FuncArgument(
                                "surface",
                                new ArrayList<>(),
                                false
                        ),
                        new FuncArgument(
                                "font",
                                new ArrayList<>(),
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
    public QObject action(Runtime runtime, HashMap<String, QObject> args) throws RuntimeStriker {
        if (!(args.get("surface") instanceof QMLSurface))
            Runtime.error("Not a surface");
        QMLSurface surface = ((QMLSurface) args.get("surface"));
        if (args.get("font") instanceof QString) {
            surface.graphics.setFont(
                    new Font(
                            args.get("font").toString(),
                            (int) args.get("style").numValue(),
                            (int) args.get("size").numValue()
                    )
            );
        } else if (args.get("font") instanceof QJavaAdapter &&
                   ((QJavaAdapter<?>) args.get("font")).object instanceof Font) {
            surface.graphics.setFont(
                    ((Font) ((QJavaAdapter<?>) args.get("font")).object)
            );
        } else Runtime.error("Font should either be a loaded font or a font name, but not " +
                args.get("font").toString());

        return QObject.Val();
    }

}
