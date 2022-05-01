package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.libmanagement.Library;
import me.tapeline.quailj.runtime.builtins.library_random.RandomFuncToss;
import me.tapeline.quailj.types.QType;

import java.util.HashMap;

public class LibraryCanvas extends Library {


    @Override
    public String getName() {
        return "canvas";
    }

    @Override
    public HashMap<String, QType> getContents() {
        HashMap<String, QType> lib = new HashMap<>();
        lib.put("newcanvas", new CanvasFuncNewcanvas());
        return lib;
    }
}
