package me.tapeline.quailj.runtime.std.qml.screen.window;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.std.qml.screen.surface.QMLSurface;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class WindowFuncStampSurface extends QBuiltinFunc {

    public WindowFuncStampSurface(Runtime runtime) {
        super(
                "stampSurface",
                Arrays.asList(
                        new FuncArgument(
                                "window",
                                new ArrayList<>(),
                                false
                        ),
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
        if (!(args.get("window") instanceof QMLWindow))
            Runtime.error("Not a window");
        QMLWindow window = (QMLWindow) args.get("window");
        if (!(args.get("surface") instanceof QMLSurface))
            Runtime.error("Not a surface");
        QMLSurface surface = ((QMLSurface) args.get("surface"));
        window.frame.getGraphics().drawImage(
                surface.image,
                0, 0,
                null
        );
        window.canvas.image = surface.image;
        window.canvas.getGraphics().drawImage(
                surface.image,
                0, 0,
                null
        );
        try {
            ImageIO.write(surface.image, "png", new File("test.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return QObject.Val();
    }

}
