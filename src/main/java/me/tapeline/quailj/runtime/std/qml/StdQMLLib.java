package me.tapeline.quailj.runtime.std.qml;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.std.qml.image.QMLImageFuncLoadImage;
import me.tapeline.quailj.runtime.std.qml.image.QMLImageFuncSaveImage;
import me.tapeline.quailj.runtime.std.qml.screen.surface.QMLSurface;
import me.tapeline.quailj.runtime.std.qml.screen.window.QMLWindow;
import me.tapeline.quailj.runtime.std.qml.sound.QMLSoundFuncLoadSound;
import me.tapeline.quailj.runtime.std.qml.sound.QMLSoundFuncPlaySound;
import me.tapeline.quailj.runtime.std.qml.sound.QMLSoundFuncStopSound;
import me.tapeline.quailj.runtime.std.qml.typography.QMLTypographyFuncCreateFont;
import me.tapeline.quailj.runtime.std.qml.typography.QMLTypographyFuncEditFont;
import me.tapeline.quailj.runtime.std.qml.typography.QMLTypographyFuncLoadFont;
import me.tapeline.quailj.runtime.std.qml.typography.QMLTypographyFuncMetrics;
import me.tapeline.quailj.typing.objects.QObject;

import java.util.HashMap;

public class StdQMLLib {

    public static QObject getLibrary(Runtime runtime) {
        HashMap<String, QObject> lib = new HashMap<>();
        HashMap<String, QObject> libScreen = new HashMap<>();
        libScreen.put("Window", QMLWindow.definition(runtime));
        libScreen.put("Surface", QMLSurface.definition(runtime));

        HashMap<String, QObject> libSound = new HashMap<>();
        libSound.put("loadSound", new QMLSoundFuncLoadSound(runtime));
        libSound.put("playSound", new QMLSoundFuncPlaySound(runtime));
        libSound.put("stopSound", new QMLSoundFuncStopSound(runtime));

        HashMap<String, QObject> libImage = new HashMap<>();
        libImage.put("loadImage", new QMLImageFuncLoadImage(runtime));
        libImage.put("saveImage", new QMLImageFuncSaveImage(runtime));

        HashMap<String, QObject> libTypography = new HashMap<>();
        libImage.put("createFont", new QMLTypographyFuncCreateFont(runtime));
        libImage.put("loadFont", new QMLTypographyFuncLoadFont(runtime));
        libImage.put("editFont", new QMLTypographyFuncEditFont(runtime));
        libImage.put("metrics", new QMLTypographyFuncMetrics(runtime));
        libImage.put("REGULAR", QObject.Val(0));
        libImage.put("ITALIC", QObject.Val(1));
        libImage.put("BOLD", QObject.Val(2));

        lib.put("screen", QObject.Val(libScreen));
        lib.put("sound", QObject.Val(libSound));
        lib.put("image", QObject.Val(libImage));
        lib.put("typography", QObject.Val(libTypography));
        QObject o = QObject.Val(lib);
        o.setPrototypeFlag(true);
        o.isInheritable = false;
        o.isDict = true;
        return o;
    }

}
