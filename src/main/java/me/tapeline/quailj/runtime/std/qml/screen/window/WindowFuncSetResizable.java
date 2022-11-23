package me.tapeline.quailj.runtime.std.qml.screen.window;

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

public class WindowFuncSetResizable extends QBuiltinFunc {

    public WindowFuncSetResizable(Runtime runtime) {
        super(
                "setResizable",
                Arrays.asList(
                        new FuncArgument(
                                "window",
                                new ArrayList<>(),
                                false
                        ),
                        new FuncArgument(
                                "flag",
                                Arrays.asList(new TypeModifier(TokenType.TYPE_BOOL)),
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
        ((QMLWindow) args.get("window")).frame.setResizable(args.get("flag").isTrue());
        return QObject.Val();
    }

}
