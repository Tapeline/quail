package me.tapeline.quailj.parsing.nodes.effect;

import me.tapeline.quailj.lexing.Token;

public class LibraryPath {

    public String[] pathComponents;
    public boolean isSlashPath;

    public LibraryPath(String path) {
        isSlashPath = path.startsWith("\"") && path.endsWith("\"");
        if (isSlashPath)
            pathComponents = path.substring(1, path.length() - 2).split("/");
        else
            pathComponents = path.split("\\.");
    }

    public LibraryPath(Token path) {
        isSlashPath = true;
        pathComponents = path.getLexeme().split("/");
    }

    public LibraryPath(String[] pathComponents, boolean isSlashPath) {
        this.pathComponents = pathComponents;
        this.isSlashPath = isSlashPath;
    }

    public String getActualLibraryDomain() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pathComponents.length; i++) {
            if (pathComponents[i].equals("*"))
                break;
            sb.append(pathComponents[i]);
            if (i + 1 < pathComponents.length)
                sb.append(".");
        }
        return sb.toString();
    }

    public String getActualLibraryPath() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pathComponents.length; i++) {
            if (pathComponents[i].equals("*"))
                break;
            sb.append(pathComponents[i]);
            if (i + 1 < pathComponents.length && !pathComponents[i+1].equals("*"))
                sb.append("/");
        }
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pathComponents.length; i++) {
            sb.append(pathComponents[i]);
            if (i + 1 < pathComponents.length)
                sb.append(".");
        }
        return sb.toString();
    }

    public boolean isExtracting() {
        return pathComponents[pathComponents.length - 1].equals("*");
    }

}
