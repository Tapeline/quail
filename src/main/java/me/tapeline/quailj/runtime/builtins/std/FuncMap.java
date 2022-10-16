package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FuncMap extends FuncType {

    public FuncMap() {
        super("map", Arrays.asList("f", "l"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.require(a.size() == 2, "func map:invalid args size");
        Assert.require(QType.isFunc(a.get(0)), "func map:mapping should be function");
        Assert.require(QType.isList(a.get(1)), "func map:arg2 is not mappable");
        ListType r = new ListType();
        for (int i = 0; i < ((ListType) a.get(1)).values.size(); i++) {
            r.values.add(((FuncType) a.get(0)).run(runtime, Collections.singletonList(
                    ((ListType) a.get(1)).values.get(1)
            )));
        }
        return r;
    }

    @Override
    public QType copy() {
        return new FuncMap();
    }
}
