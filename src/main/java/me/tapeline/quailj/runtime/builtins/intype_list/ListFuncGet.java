package me.tapeline.quailj.runtime.builtins.intype_list;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class ListFuncGet extends FuncType {

    public ListFuncGet() {
        super("get", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 2, "list get:invalid args size");
        Assert.require(QType.isList(a.get(0)) && QType.isNum(a.get(1)), "list get:invalid types");
        return ((ListType) a.get(0)).values.get((int) ((NumType) a.get(1)).value);
    }

    @Override
    public QType copy() {
        return new ListFuncGet();
    }
}
