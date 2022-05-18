package me.tapeline.quailj.runtime.builtins.intype_string;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class StringFuncReverse extends FuncType {

    public StringFuncReverse() {
        super("reverse", Arrays.asList("str"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 1, "string reverse:invalid args size");
        Assert.require(a.get(0) instanceof StringType, "string reverse:invalid arg0 type");
        StringBuilder sb = new StringBuilder(((StringType) a.get(0)).value);
        sb.reverse();
        return QType.V(sb.toString());
    }

    @Override
    public QType copy() {
        return new StringFuncReverse();
    }
}
