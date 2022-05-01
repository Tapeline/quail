package me.tapeline.quailj.runtime.builtins.intype_container;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ContainerFuncGet extends FuncType {

    public ContainerFuncGet() {
        super("get", Arrays.asList("c", "key"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 2, "container get:invalid args size");
        Assert.require(a.get(0) instanceof ContainerType, "container get:invalid arg0 type");
        Assert.require(a.get(1) instanceof StringType, "container get:invalid arg1 type");
        return ((ContainerType) a.get(0)).table.get(((StringType) a.get(1)).value);
    }

    @Override
    public QType copy() {
        return new ContainerFuncGet();
    }
}
