package me.tapeline.quailj.runtime.builtins.intype_container;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class ContainerFuncContains extends FuncType {

    public ContainerFuncContains() {
        super("contains", Arrays.asList("c", "key"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 2, "container contains:invalid args size");
        Assert.require(a.get(0).v instanceof ContainerType, "container contains:invalid arg0 type");
        Assert.require(a.get(1).v instanceof StringType, "container contains:invalid arg1 type");
        return new QValue(a.get(0).v.table.containsKey(((StringType) a.get(1).v).value));
    }

    @Override
    public QType copy() {
        return new ContainerFuncContains();
    }
}
