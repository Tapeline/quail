package me.tapeline.quailj.runtime.std.storage;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;

import java.util.HashMap;

public class StdStorageLib {

    public static QObject getLibrary(Runtime runtime) {
        HashMap<String, QObject> lib = new HashMap<>();
        lib.put("loadYaml",     new StorageFuncLoadYaml(runtime));
        lib.put("saveYaml",     new StorageFuncSaveYaml(runtime));
        QObject o = QObject.Val(lib);
        o.setPrototypeFlag(true);
        o.isInheritable = false;
        o.isDict = true;
        return o;
    }

}
