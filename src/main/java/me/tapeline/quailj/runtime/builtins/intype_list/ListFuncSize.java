package me.tapeline.quailj.runtime.builtins.intype_list;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListFuncSize extends FuncType {

    public ListFuncSize() {
        super("size", Arrays.asList("l"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 1, "list size:invalid args size");
        Assert.require(a.get(0) instanceof ListType, "list size:invalid arg0 type");
        return new NumType(((ListType) a.get(0)).values.size());
    }

    @Override
    public QType copy() {
        return new ListFuncSize();
    }
}
