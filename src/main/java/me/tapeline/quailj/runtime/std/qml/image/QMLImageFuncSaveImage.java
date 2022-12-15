package me.tapeline.quailj.runtime.std.qml.image;

import me.tapeline.quailj.lexing.TokenType;
import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.std.qml.screen.surface.QMLSurface;
import me.tapeline.quailj.typing.modifiers.TypeModifier;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class QMLImageFuncSaveImage extends QBuiltinFunc {

    public QMLImageFuncSaveImage(Runtime runtime) {
        super(
                "saveImage",
                Arrays.asList(
                        new FuncArgument(
                                "path",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_STRING)),
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
        if (!(args.get("surface") instanceof QMLSurface))
            Runtime.error("Not a surface");
        QMLSurface surface = ((QMLSurface) args.get("surface"));
        try {
            return QObject.Val(ImageIO.write(surface.image,
                    "png", new File(args.get("path").toString())));
        } catch (IOException e) {
            Runtime.error("Unexpected exception during image opening:\n" + e.toString() +
                    "\n" + e.getLocalizedMessage());
            return QObject.Val();
        }
    }

}
