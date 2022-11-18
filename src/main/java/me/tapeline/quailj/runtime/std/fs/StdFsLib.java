package me.tapeline.quailj.runtime.std.fs;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;

import java.util.HashMap;

public class StdFsLib {

    public static QObject getLibrary(Runtime runtime) {
        HashMap<String, QObject> lib = new HashMap<>();
        lib.put("absolutePath",     new FsFuncAbsolutePath(runtime));
        lib.put("canExecute",       new FsFuncCanExecute(runtime));
        lib.put("canRead",          new FsFuncCanRead(runtime));
        lib.put("canWrite",         new FsFuncCanWrite(runtime));
        lib.put("createBlank",      new FsFuncCreateBlank(runtime));
        lib.put("delete",           new FsFuncDelete(runtime));
        lib.put("deleteFile",       new FsFuncDeleteFile(runtime));
        lib.put("deleteFolder",     new FsFuncDeleteFolder(runtime));
        lib.put("dirContents",      new FsFuncDirContents(runtime));
        lib.put("exists",           new FsFuncExists(runtime));
        lib.put("fileName",         new FsFuncFileName(runtime));
        lib.put("isDirectory",      new FsFuncIsDirectory(runtime));
        lib.put("isFile",           new FsFuncIsFile(runtime));
        lib.put("isHidden",         new FsFuncIsHidden(runtime));
        lib.put("makeReadOnly",     new FsFuncMakeReadOnly(runtime));
        lib.put("mkdirs",           new FsFuncMkdirs(runtime));
        lib.put("read",             new FsFuncRead(runtime));
        lib.put("readBinary",       new FsFuncReadBinary(runtime));
        lib.put("setExecutable",    new FsFuncSetExecutable(runtime));
        lib.put("setWritable",      new FsFuncSetWritable(runtime));
        lib.put("setReadable",      new FsFuncSetReadable(runtime));
        lib.put("write",            new FsFuncWrite(runtime));
        lib.put("writeBinary",      new FsFuncWriteBinary(runtime));
        QObject o = QObject.Val(lib);
        o.setPrototypeFlag(true);
        o.isInheritable = false;
        o.isDict = true;
        return o;
    }

}
