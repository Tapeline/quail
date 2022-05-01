package me.tapeline.quailj.runtime.builtins.intype_list;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListFuncAdd extends FuncType {

    public ListFuncAdd() {
        super("add", Arrays.asList("l", "val"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 2, "list add:invalid args size");
        Assert.require(a.get(0) instanceof ListType, "list add:invalid arg0 type");
        Assert.require(a.get(1) != null, "list add:invalid arg1 type");
        ((ListType) a.get(0)).values.add(((QType) a.get(1)));
        return new VoidType();
    }

    @Override
    public QType copy() {
        return new ListFuncAdd();
    }
}
