package me.tapeline.quailj.runtime.builtins.library_random;

import me.tapeline.quailj.libmanagement.Library;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.QValue;

import java.util.HashMap;

public class LibraryRandom extends Library {


    @Override
    public String getName() {
        return "random";
    }

    @Override
    public HashMap<String, QValue> getContents() {
        HashMap<String, QValue> lib = new HashMap<>();
        lib.put("toss", new QValue(new RandomFuncToss()));
        return lib;
    }
}
