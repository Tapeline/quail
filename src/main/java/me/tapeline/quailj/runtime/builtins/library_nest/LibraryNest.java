package me.tapeline.quailj.runtime.builtins.library_nest;

import me.tapeline.quailj.libmanagement.Library;
import me.tapeline.quailj.types.QType;

import java.util.HashMap;

public class LibraryNest extends Library {
    @Override
    public String getName() {
        return "nest";
    }

    @Override
    public HashMap<String, QType> getContents() {
        HashMap<String, QType> lib = new HashMap<>();
        lib.put("pack", new NestFuncPack());
        lib.put("unpack", new NestFuncUnpack());
        return lib;
    }
}
