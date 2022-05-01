package me.tapeline.quailj.runtime.builtins.intype_nums;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class NumFuncRound extends FuncType {

    public NumFuncRound() {
        super("round", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.require(QType.isNum(a.get(0).v), "round:NaN");
        return new QValue(Math.round(((NumType) a.get(0).v).value));
    }

    @Override
    public QType copy() {
        return new NumFuncRound();
    }
}
