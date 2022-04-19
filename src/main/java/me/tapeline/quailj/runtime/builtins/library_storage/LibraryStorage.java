package me.tapeline.quailj.runtime.builtins.library_storage;

import me.tapeline.quailj.libmanagement.Library;
import me.tapeline.quailj.runtime.builtins.library_random.RandomFuncToss;
import me.tapeline.quailj.types.QType;

import java.util.HashMap;

public class LibraryStorage extends Library {


    @Override
    public String getName() {
        return "storage";
    }

    @Override
    public HashMap<String, QType> getContents() {
        HashMap<String, QType> lib = new HashMap<>();
        lib.put("loadyml", new StorageFuncLoadyml());
        lib.put("saveyml", new StorageFuncSaveyml());
        return lib;
    }
}
