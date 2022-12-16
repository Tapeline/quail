package me.tapeline.quailj.runtime.std.ji;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;

import java.util.HashMap;

public class StdJiLib {

    public static QObject getLibrary(Runtime runtime) {
        HashMap<String, QObject> lib = new HashMap<>();
        lib.put("int",     new JiFuncInt(runtime));
        lib.put("boolean", new JiFuncBoolean(runtime));
        lib.put("float",   new JiFuncFloat(runtime));
        lib.put("double",  new JiFuncDouble(runtime));
        lib.put("long",    new JiFuncLong(runtime));
        lib.put("short",   new JiFuncShort(runtime));
        lib.put("byte",    new JiFuncByte(runtime));
        lib.put("char",    new JiFuncChar(runtime));
        lib.put("getAdapter", new JiFuncGetAdapter(runtime));
        QObject o = QObject.Val(lib);
        o.setPrototypeFlag(true);
        o.isInheritable = false;
        o.isDict = true;
        return o;
    }

}
