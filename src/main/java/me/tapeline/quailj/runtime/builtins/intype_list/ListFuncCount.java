package me.tapeline.quailj.runtime.builtins.intype_list;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;
import me.tapeline.quailj.utils.Utilities;

import java.util.Collections;
import java.util.List;

public class ListFuncCount extends FuncType {

    public ListFuncCount() {
        super("count", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 2, "list count:invalid args size");
        Assert.require(QType.isList(a.get(0).v), "list count:invalid types");
        int c = 0;
        for (QValue v : ((ListType) a.get(0).v).values)
            if (Utilities.compare(v, a.get(1)).value) c++;
        return new QValue(c);
    }

    @Override
    public QType copy() {
        return new ListFuncCount();
    }
}
