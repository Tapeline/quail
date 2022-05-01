package me.tapeline.quailj.runtime.builtins.intype_nums;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class NumFuncFloor extends FuncType {

    public NumFuncFloor() {
        super("floor", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.require(QType.isNum(a.get(0).v), "floor:NaN");
        return new QValue(Math.floor(((NumType) a.get(0).v).value));
    }

    @Override
    public QType copy() {
        return new NumFuncFloor();
    }
}
