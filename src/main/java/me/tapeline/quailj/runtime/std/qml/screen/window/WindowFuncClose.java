package me.tapeline.quailj.runtime.std.qml.screen.window;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class WindowFuncClose extends QBuiltinFunc {

    public WindowFuncClose(Runtime runtime) {
        super(
                "close",
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
        ((QMLWindow) args.get("window")).frame.dispose();
        return QObject.Val();
    }

}
