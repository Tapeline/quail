package me.tapeline.quailj.runtime.builtins.library_internals;

import me.tapeline.quailj.libmanagement.Library;
import me.tapeline.quailj.types.QType;

import java.util.HashMap;

public class LibraryInternals extends Library {


    @Override
    public String getName() {
        return "internals";
    }

    @Override
    public HashMap<String, QType> getContents() {
        HashMap<String, QType> lib = new HashMap<>();
        lib.put("enclosingSet",  new InternalsFuncEnclosingSet());
        lib.put("enclosingGet",  new InternalsFuncEnclosingGet());
        lib.put("globalSet",  new InternalsFuncGlobalSet());
        lib.put("globalGet",  new InternalsFuncGlobalGet());
        return lib;
    }
}
