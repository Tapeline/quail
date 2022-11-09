package me.tapeline.quailj.runtime.libraries;

import me.tapeline.quailj.typing.objects.QObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class LibraryRegistry {

    private HashMap<String, QObject> cachedLibraries = new HashMap<>();
    public Set<String> libraryRoots = new HashSet<>();
    public LibraryRegistry() {}

    public void cacheLibrary(String name, QObject library) {
        cachedLibraries.put(name, library);
    }

    public QObject getLibrary(String name) {
        return cachedLibraries.get(name);
    }

}
