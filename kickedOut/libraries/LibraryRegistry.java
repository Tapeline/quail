package me.tapeline.quailj.runtime.libraries;

import me.tapeline.quailj.runtime.libraries.domain.LibraryDomain;
import me.tapeline.quailj.typing.objects.QObject;

import java.util.HashSet;
import java.util.Set;

public class LibraryRegistry {

    private final LibraryDomain superDomain = new LibraryDomain("/");
    public Set<String> libraryRoots = new HashSet<>();
    public LibraryRegistry() {}

    public LibraryDomain getDomain(String domain) {
        return superDomain.getDomain(domain);
    }

    public void cacheLibrary(String domain, QObject library) {
        LibraryDomain target = getDomain(domain);
        if (target == null)
            target = superDomain.createDomain(0, domain.split("\\."));
        target.setLibraryHere(library);
    }

    public QObject getLibrary(String domain) {
        LibraryDomain target = getDomain(domain);
        if (target == null)
            return null;
        return target.libraryHere;
    }

}
