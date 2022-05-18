package me.tapeline.quailj.runtime.builtins.intype_container;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class ContainerFuncRemove extends FuncType {

    public ContainerFuncRemove() {
        super("remove", Arrays.asList("c", "key"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 2, "container remove:invalid args size");
        Assert.require(a.get(0) instanceof ContainerType, "container remove:invalid arg0 type");
        Assert.require(a.get(1) instanceof StringType, "container remove:invalid arg1 type");
        a.get(0).table.remove(((StringType) a.get(1)).value);
        return QType.V();
    }

    @Override
    public QType copy() {
        return new ContainerFuncRemove();
    }
}
