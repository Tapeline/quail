package me.tapeline.quailj.libmanagement;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.AbstractFunc;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;

import java.util.List;

public abstract class AbstractMethodAction {

    public abstract QType run(AbstractFunc func, Runtime runtime, List<QType> a) throws RuntimeStriker;

}
