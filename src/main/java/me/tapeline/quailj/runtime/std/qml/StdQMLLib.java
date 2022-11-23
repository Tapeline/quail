package me.tapeline.quailj.runtime.std.qml;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.std.qml.screen.window.QMLWindow;
import me.tapeline.quailj.runtime.std.qml.sound.QMLSoundFuncLoadSound;
import me.tapeline.quailj.runtime.std.qml.sound.QMLSoundFuncPlaySound;
import me.tapeline.quailj.runtime.std.qml.sound.QMLSoundFuncStopSound;
import me.tapeline.quailj.typing.objects.QObject;

import java.util.HashMap;

public class StdQMLLib {

    public static QObject getLibrary(Runtime runtime) {
        HashMap<String, QObject> lib = new HashMap<>();
        HashMap<String, QObject> libScreen = new HashMap<>();
        libScreen.put("Window", QMLWindow.definition(runtime));

        HashMap<String, QObject> libSound = new HashMap<>();
        libSound.put("loadSound", new QMLSoundFuncLoadSound(runtime));
        libSound.put("playSound", new QMLSoundFuncPlaySound(runtime));
        libSound.put("stopSound", new QMLSoundFuncStopSound(runtime));

        lib.put("screen", QObject.Val(libScreen));
        QObject o = QObject.Val(lib);
        o.setPrototypeFlag(true);
        o.isInheritable = false;
        o.isDict = true;
        return o;
    }

}
