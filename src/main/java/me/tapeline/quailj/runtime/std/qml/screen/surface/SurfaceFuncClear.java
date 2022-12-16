package me.tapeline.quailj.runtime.std.qml.screen.surface;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SurfaceFuncClear extends QBuiltinFunc {

    public SurfaceFuncClear(Runtime runtime) {
        super(
                "clear",
                Collections.singletonList(
                        new FuncArgument(
                                "surface",
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
        surface.graphics.clearRect(
                0, 0,
                surface.image.getWidth(),
                surface.image.getHeight()
        );
        return QObject.Val();
    }

}
