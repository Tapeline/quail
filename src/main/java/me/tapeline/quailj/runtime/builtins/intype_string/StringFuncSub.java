package me.tapeline.quailj.runtime.builtins.intype_string;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class StringFuncSub extends FuncType {

    public StringFuncSub() {
        super("sub", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 2, "string sub:invalid args size");
        Assert.require(QType.isStr(a.get(0).v) && QType.isNum(a.get(1).v) && (a.size() == 3? QType.isNum(a.get(2).v) : true),
                "string sub:invalid types");
        if (a.size() == 2) {
            return new QValue("" + ((StringType) a.get(0).v).value.substring((int) ((NumType) a.get(1).v).value));
        } else if (a.size() > 2) {
            return new QValue("" + ((StringType) a.get(0).v).value.substring(
                    (int) ((NumType) a.get(1).v).value,
                    (int) ((NumType) a.get(2).v).value));
        }
        return new QValue();
    }

    @Override
    public QType copy() {
        return new StringFuncSub();
    }
}
