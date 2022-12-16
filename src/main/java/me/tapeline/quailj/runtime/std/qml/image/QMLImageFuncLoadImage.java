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
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;

public class QMLImageFuncLoadImage extends QBuiltinFunc {

    public QMLImageFuncLoadImage(Runtime runtime) {
        super(
                "loadImage",
                Collections.singletonList(
                        new FuncArgument(
                                "path",
                                Collections.singletonList(new TypeModifier(TokenType.TYPE_STRING)),
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
        try {
            URL url = new URL(args.get("path").toString());
            BufferedImage img = ImageIO.read(url);
            return new QMLSurface(runtime, img);
        } catch (IOException e) {
            Runtime.error("Unexpected exception during image opening:\n" + e +
                    "\n" + e.getLocalizedMessage());
            return QObject.Val();
        }
    }

}
