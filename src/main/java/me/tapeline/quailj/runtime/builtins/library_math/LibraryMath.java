package me.tapeline.quailj.runtime.builtins.library_math;

import me.tapeline.quailj.libmanagement.Library;
import me.tapeline.quailj.runtime.builtins.library_nest.NestFuncPack;
import me.tapeline.quailj.runtime.builtins.library_nest.NestFuncUnpack;
import me.tapeline.quailj.types.QType;

import java.util.HashMap;

public class LibraryMath extends Library {
    @Override
    public String getName() {
        return "math";
    }

    @Override
    public HashMap<String, QType> getContents() {
        HashMap<String, QType> lib = new HashMap<>();
        lib.put("e", QType.V(Math.E));
        lib.put("pi", QType.V(Math.PI));
        lib.put("hyp", new MathFuncHyp());
        return lib;
    }
}
