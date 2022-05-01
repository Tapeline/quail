package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.libmanagement.Library;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.QValue;

import java.util.HashMap;

public class LibraryCanvas extends Library {


    @Override
    public String getName() {
        return "canvas";
    }

    @Override
    public HashMap<String, QValue> getContents() {
        HashMap<String, QValue> lib = new HashMap<>();
        lib.put("newcanvas", new QValue(new CanvasFuncNewcanvas()));
        return lib;
    }
}
