package me.tapeline.quailj.runtime.builtins.intype_string;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class StringFuncReplace extends FuncType {

    public StringFuncReplace() {
        super("replace", Collections.singletonList(""), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 2, "string replace:invalid args size");
        Assert.require(QType.isStr(a.get(0).v, a.get(1).v, a.get(2).v), "string replace:invalid types");
        return new QValue("" + ((StringType) a.get(0).v).value.replace(
                ((StringType) a.get(1).v).value,
                ((StringType) a.get(2).v).value));
    }

    @Override
    public QType copy() {
        return new StringFuncReplace();
    }
}
