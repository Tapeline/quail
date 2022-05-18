package me.tapeline.quailj.runtime.builtins.library_nest;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.FuncType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;
import me.tapeline.quailj.utils.Assert;

import java.util.Collections;
import java.util.List;

public class NestFuncUnpack extends FuncType {

    public NestFuncUnpack() {
        super("unpack", Collections.singletonList("obj"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.require(a.size() > 0, "func unpack:invalid args size");
        Assert.require(QType.isStr(a.get(0)), "func unpack:value should be base64-string");
        return QType.V();
        //return QType.unpack(a.get(0).toString());
    }

    @Override
    public QType copy() {
        return new NestFuncUnpack();
    }
}
