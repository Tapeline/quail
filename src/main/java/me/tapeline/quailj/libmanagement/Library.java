package me.tapeline.quailj.libmanagement;

import me.tapeline.quailj.typing.objects.QObject;

import java.util.HashMap;

public abstract class Library {

    public abstract String getName();

    public abstract HashMap<String, QObject> getContents();

}
