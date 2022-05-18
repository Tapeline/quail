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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 2, "container contains:invalid args size");
        Assert.require(a.get(0) instanceof ContainerType, "container contains:invalid arg0 type");
        Assert.require(a.get(1) instanceof StringType, "container contains:invalid arg1 type");
        return QType.V(a.get(0).table.containsKey(((StringType) a.get(1)).value));
    }

    @Override
    public QType copy() {
        return new ContainerFuncContains();
    }
}
