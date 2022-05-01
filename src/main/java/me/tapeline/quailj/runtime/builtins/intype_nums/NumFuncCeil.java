package me.tapeline.quailj.runtime.builtins.intype_nums;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.types.NumType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class NumFuncCeil extends FuncType {

    public NumFuncCeil() {
        super("ceil", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.require(QType.isNum(a.get(0).v), "ceil:NaN");
        return new QValue(Math.ceil(((NumType) a.get(0).v).value));
    }

    @Override
    public QType copy() {
        return new NumFuncCeil();
    }
}
