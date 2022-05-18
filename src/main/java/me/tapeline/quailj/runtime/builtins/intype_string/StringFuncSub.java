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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 2, "string sub:invalid args size");
        Assert.require(QType.isStr(a.get(0)) && QType.isNum(a.get(1)) && (a.size() == 3? QType.isNum(a.get(2)) : true),
                "string sub:invalid types");
        if (a.size() == 2) {
            return QType.V("" + ((StringType) a.get(0)).value.substring((int) ((NumType) a.get(1)).value));
        } else if (a.size() > 2) {
            return QType.V("" + ((StringType) a.get(0)).value.substring(
                    (int) ((NumType) a.get(1)).value,
                    (int) ((NumType) a.get(2)).value));
        }
        return QType.V();
    }

    @Override
    public QType copy() {
        return new StringFuncSub();
    }
}
