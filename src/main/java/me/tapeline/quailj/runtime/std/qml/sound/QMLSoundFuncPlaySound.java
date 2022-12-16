package me.tapeline.quailj.runtime.std.qml.sound;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QJavaAdapter;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;
import me.tapeline.quailj.typing.objects.funcutils.FuncArgument;
import me.tapeline.quailj.typing.objects.funcutils.QBuiltinFunc;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class QMLSoundFuncPlaySound extends QBuiltinFunc {

    public QMLSoundFuncPlaySound(Runtime runtime) {
        super(
                "playSound",
                Collections.singletonList(
                        new FuncArgument(
                                "sound",
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
        if (!(args.get("sound") instanceof QJavaAdapter) ||
            !(((QJavaAdapter<?>) args.get("sound")).object instanceof Clip))
            Runtime.error("Not a JavaAdapter<Clip>");
        ((Clip) ((QJavaAdapter<?>) args.get("sound")).object).start();
        return QObject.Val();
    }

}
