package me.tapeline.quailj.runtime.builtins.intype_list;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class ListFuncReverse extends FuncType {

    public ListFuncReverse() {
        super("reverse", Arrays.asList("l"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 1, "list reverse:invalid args size");
        Assert.require(a.get(0) instanceof ListType, "list reverse:invalid arg0 type");
        ListType l = new ListType();
        for (int i = 0; i < ((ListType) a.get(0)).values.size(); i++)
            l.values.add(((ListType) a.get(0)).values.get(((ListType) a.get(0)).values.size() - i - 1));
        return l;
    }

    @Override
    public QType copy() {
        return new ListFuncReverse();
    }
}
