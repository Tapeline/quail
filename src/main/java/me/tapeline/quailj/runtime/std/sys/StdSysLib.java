package me.tapeline.quailj.runtime.std.sys;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.std.fs.*;
import me.tapeline.quailj.typing.objects.QObject;

import java.util.HashMap;

public class StdSysLib {

    public static QObject getLibrary(Runtime runtime) {
        HashMap<String, QObject> lib = new HashMap<>();

        lib.put("args",                 QObject.Val(runtime.consoleArgsQObjects));
        lib.put("clearProperty",        new SysFuncClearProperty(runtime));
        lib.put("exit",                 new SysFuncExit(runtime));
        lib.put("getProperty",          new SysFuncGetProperty(runtime));
        lib.put("setProperty",          new SysFuncSetProperty(runtime));
        QObject o = QObject.Val(lib);
        o.setPrototypeFlag(true);
        o.isInheritable = false;
        o.isDict = true;
        return o;
    }

}
