package me.tapeline.quailj.runtime.builtins.library_random;

import me.tapeline.quailj.libmanagement.Library;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.QType;

import java.util.HashMap;

public class LibraryRandom extends Library {


    @Override
    public String getName() {
        return "random";
    }

    @Override
    public HashMap<String, QType> getContents() {
        HashMap<String, QType> lib = new HashMap<>();
        lib.put("toss",  new RandomFuncToss());
        lib.put("random", new RandomFuncRandom());
        return lib;
    }
}
