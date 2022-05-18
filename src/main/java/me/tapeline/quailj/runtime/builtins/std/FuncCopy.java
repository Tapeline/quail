package me.tapeline.quailj.runtime.builtins.std;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class FuncCopy extends FuncType {

    public FuncCopy() {
        super("copy", Collections.singletonList("obj"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.require(a.size() > 0, "func copy:invalid args size");
        return a.get(0).copy();
    }

    @Override
    public QType copy() {
        return new FuncCopy();
    }
}
