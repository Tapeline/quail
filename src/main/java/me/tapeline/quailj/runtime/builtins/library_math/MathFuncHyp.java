package me.tapeline.quailj.runtime.builtins.library_math;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class MathFuncHyp extends FuncType {

    public MathFuncHyp() {
        super("hyp", Collections.singletonList("obj"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.require(a.size() == 2, "math hyp:invalid args size");
        Assert.require(QType.isNum(a.get(0), a.get(1)), "math hyp:not a number");
        return QType.V(Math.hypot(((NumType) a.get(0)).value,
                ((NumType) a.get(1)).value));
    }

    @Override
    public QType copy() {
        return new MathFuncHyp();
    }
}
