package me.tapeline.quailj.runtime.libraries.domain;

import me.tapeline.quailj.typing.objects.QObject;

import java.util.HashSet;
import java.util.Set;

public class LibraryDomain {

    public Set<LibraryDomain> subdomains = new HashSet<>();
    public String domain;
    public QObject libraryHere = null;

    public LibraryDomain(String domain) {
        this.domain = domain;
    }

    public LibraryDomain(String domain, QObject libraryHere) {
        this.domain = domain;
        this.libraryHere = libraryHere;
    }

    public void add(LibraryDomain domain) {
        subdomains.add(domain);
    }

    public void setLibraryHere(QObject libraryHere) {
        this.libraryHere = libraryHere;
    }

    public LibraryDomain getDomain(String domain) {
        String[] domainPath = domain.split("\\.");
        for (LibraryDomain subdomain : subdomains)
            if (subdomain.domain.equals(domainPath[0]) && domainPath.length == 1)
                return subdomain;
            else if (subdomain.domain.equals(domainPath[0]))
                return getDomain(1, domainPath);
        return null;
    }

    public LibraryDomain getDomain(int offset, String[] domainPath) {
        for (LibraryDomain subdomain : subdomains)
            if (offset == domainPath.length)
                return this;
            else if (subdomain.domain.equals(domainPath[offset]))
                return getDomain(offset + 1, domainPath);
        return null;
    }

    public boolean hasDomain(String domain) {
        for (LibraryDomain subdomain : subdomains)
            if (subdomain.domain.equals(domain))
                return true;
        return false;
    }

    public LibraryDomain createDomain(int offset, String[] domainPath) {
        if (offset == domainPath.length)
            return this;
        if (hasDomain(domainPath[offset]))
            return getDomain(domainPath[offset]).createDomain(offset + 1, domainPath);
        else {
            LibraryDomain newDomain = new LibraryDomain(domainPath[offset]);
            subdomains.add(newDomain);
            return newDomain.createDomain(offset + 1, domainPath);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LibraryDomain)
            return ((LibraryDomain) obj).domain.equals(domain);
        return super.equals(obj);
    }

}
