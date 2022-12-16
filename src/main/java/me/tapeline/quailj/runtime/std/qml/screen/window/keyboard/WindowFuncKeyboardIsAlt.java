package me.tapeline.quailj.runtime.std.qml.screen.window.keyboard;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.std.qml.screen.window.QMLWindow;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class WindowFuncKeyboardIsAlt extends QBuiltinFunc {

    public WindowFuncKeyboardIsAlt(Runtime runtime) {
        super(
                "keyboardIsAlt",
                Collections.singletonList(
                        new FuncArgument(
                                "window",
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
        QMLWindow window = ((QMLWindow) args.get("window"));
        return QObject.Val(window.keyboardHandler.isAlt);
    }

}
