package me.tapeline.quailj.runtime.builtins.intype_container;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class ContainerFuncAssemble extends FuncType {

    public ContainerFuncAssemble() {
        super("assemble", Arrays.asList("c", "keys", "values"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 3, "container assemble:invalid args size");
        Assert.require(a.get(0) instanceof ContainerType, "container assemble:invalid arg0 type");
        Assert.require(a.get(1) instanceof ListType, "container assemble:invalid arg1 type");
        Assert.require(a.get(2) instanceof ListType, "container assemble:invalid arg2 type");
        for (int i = 0; i < Math.min(((ListType) a.get(1)).values.size(),
                                    ((ListType) a.get(2)).values.size()); i++)
            a.get(0).table.put(((ListType) a.get(1)).values.get(i).toString(),
                                ((ListType) a.get(2)).values.get(i));
        return a.get(0);
    }

    @Override
    public QType copy() {
        return new ContainerFuncAssemble();
    }
}
