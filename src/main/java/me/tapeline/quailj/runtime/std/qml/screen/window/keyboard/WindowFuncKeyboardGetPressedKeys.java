package me.tapeline.quailj.runtime.std.qml.screen.window.keyboard;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.std.qml.screen.window.QMLWindow;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WindowFuncKeyboardGetPressedKeys extends QBuiltinFunc {

    public WindowFuncKeyboardGetPressedKeys(Runtime runtime) {
        super(
                "keyboardGetPressedKeys",
                Arrays.asList(
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
        List<QObject> l = new ArrayList<>();
        for (Character c : window.keyboardHandler.pressed)
            l.add(QObject.Val(c.toString()));
        return QObject.Val(l);
    }

}
