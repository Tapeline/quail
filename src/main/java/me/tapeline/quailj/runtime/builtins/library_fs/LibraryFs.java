package me.tapeline.quailj.runtime.builtins.library_fs;

import me.tapeline.quailj.libmanagement.Library;
import me.tapeline.quailj.runtime.builtins.library_math.MathFuncHyp;
import me.tapeline.quailj.types.QType;

import java.util.HashMap;

public class LibraryFs extends Library {
    @Override
    public String getName() {
        return "fs";
    }

    @Override
    public HashMap<String, QType> getContents() {
        HashMap<String, QType> lib = new HashMap<>();
        lib.put("rm", new FsFuncRm());
        lib.put("mkdir", new FsFuncMkdir());
        lib.put("exists", new FsFuncExists());
        lib.put("ls", new FsFuncLs());
        lib.put("rename", new FsFuncRename());
        return lib;
    }
}
