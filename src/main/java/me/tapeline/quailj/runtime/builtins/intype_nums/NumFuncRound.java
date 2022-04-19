package me.tapeline.quailj.runtime.builtins.intype_nums;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.NumType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class NumFuncRound extends FuncType {

    public NumFuncRound() {
        super("round", Collections.singletonList(""), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.require(QType.isNum(a.get(0)), "round:NaN");
        return new NumType(Math.round(((NumType) a.get(0)).value));
    }

    @Override
    public QType copy() {
        return new NumFuncRound();
    }
}
