package me.tapeline.quailj.runtime.std.qml.screen.window;

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

public class WindowFuncSetSize extends QBuiltinFunc {

    public WindowFuncSetSize(Runtime runtime) {
        super(
                "setSize",
                Arrays.asList(
                        new FuncArgument(
                                "window",
                                new ArrayList<>(),
                                false
                        ),
                        new FuncArgument(
                                "w",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_NUM)),
                                false
                        ),
                        new FuncArgument(
                                "h",
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
        if (!(args.get("window") instanceof QMLWindow))
            Runtime.error("Not a window");
        Frame frame = ((QMLWindow) args.get("window")).frame;
        frame.setBounds(
                frame.getX(),
                frame.getY(),
                ((int) args.get("w").numValue()),
                ((int) args.get("h").numValue())
        );
        return QObject.Val();
    }

}
