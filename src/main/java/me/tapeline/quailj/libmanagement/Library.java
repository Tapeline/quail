package me.tapeline.quailj.libmanagement;

import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.QType;

import java.util.HashMap;

public abstract class Library {

    public abstract String getName();

    public abstract HashMap<String, QType> getContents();

}
