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
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class QMLTypographyFuncLoadFont extends QBuiltinFunc {

    public QMLTypographyFuncLoadFont(Runtime runtime) {
        super(
                "loadFont",
                Arrays.asList(
                        new FuncArgument(
                                "font",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_STRING)),
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
            return new QJavaAdapter<>(runtime, Font.createFont(
                    Font.TRUETYPE_FONT,
                    new File(args.get("font").toString())
            ));
        } catch (FontFormatException | IOException e) {
            Runtime.error("Error when loading font: \n" + e.toString() +
                    "\n" + e.getLocalizedMessage());
        }
        return QObject.Val();
    }

}
