package me.tapeline.quailj.runtime.builtins.intype_list;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ListFuncRemove extends FuncType {

    public ListFuncRemove() {
        super("remove", Arrays.asList("l", "index"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 2, "list remove:invalid args size");
        Assert.require(a.get(0) instanceof ListType, "list remove:invalid arg0 type");
        Assert.require(a.get(1) instanceof NumType, "list remove:invalid arg1 type");
        ((ListType) a.get(0)).values.remove((int) ((NumType) a.get(1)).value);
        return a.get(0);
    }

    @Override
    public QType copy() {
        return new ListFuncRemove();
    }
}
