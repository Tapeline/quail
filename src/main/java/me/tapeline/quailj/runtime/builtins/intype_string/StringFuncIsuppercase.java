package me.tapeline.quailj.runtime.builtins.intype_string;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class StringFuncIsuppercase extends FuncType {

    public StringFuncIsuppercase() {
        super("isuppercase", Arrays.asList("str"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 1, "string isuppercase:invalid args size");
        Assert.require(a.get(0).v instanceof StringType, "string isuppercase:invalid arg0 type");
        String sv = ((StringType) a.get(0).v).value;
        for (char c : sv.toCharArray())
            if (!Character.isUpperCase(c)) return new QValue(false);
        return new QValue(true);
    }

    @Override
    public QType copy() {
        return new StringFuncIsuppercase();
    }
}
