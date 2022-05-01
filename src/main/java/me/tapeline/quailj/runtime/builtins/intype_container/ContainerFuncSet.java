package me.tapeline.quailj.runtime.builtins.intype_container;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class ContainerFuncSet extends FuncType {

    public ContainerFuncSet() {
        super("set", Arrays.asList("c", "key", "value"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 3, "container set:invalid args size");
        Assert.require(a.get(0).v instanceof ContainerType, "container set:invalid arg0 type");
        Assert.require(a.get(1).v instanceof StringType, "container set:invalid arg1 type");
        Assert.require(a.get(2).v != null, "container set:invalid arg2 type");
        a.get(0).v.table.put(((StringType) a.get(1).v).value, a.get(2));
        return new QValue();
    }

    @Override
    public QType copy() {
        return new ContainerFuncSet();
    }
}
