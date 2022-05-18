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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 2, "string replace:invalid args size");
        Assert.require(QType.isStr(a.get(0), a.get(1), a.get(2)), "string replace:invalid types");
        return QType.V("" + ((StringType) a.get(0)).value.replace(
                ((StringType) a.get(1)).value,
                ((StringType) a.get(2)).value));
    }

    @Override
    public QType copy() {
        return new StringFuncReplace();
    }
}
