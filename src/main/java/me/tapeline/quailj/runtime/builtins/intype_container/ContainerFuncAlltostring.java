package me.tapeline.quailj.runtime.builtins.intype_container;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContainerFuncAlltostring extends FuncType {

    public ContainerFuncAlltostring() {
        super("alltostring", Arrays.asList("c"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 1, "container values:invalid args size");
        Assert.require(a.get(0) instanceof ContainerType, "container values:invalid arg0 type");
        return new StringType(((ContainerType) a.get(0)).allToString());
    }

    @Override
    public QType copy() {
        return new ContainerFuncAlltostring();
    }
}
