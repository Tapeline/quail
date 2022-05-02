package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class FuncTable extends FuncType {

    public FuncTable() {
        super("table", Collections.singletonList("obj"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.require(a.size() > 0, "func table:specify a value!");
        ContainerType c = new ContainerType(a.get(0).v.table);
        c.table = a.get(0).v.table;
        return new QValue(c);
    }

    @Override
    public QType copy() {
        return new FuncTable();
    }
}
