package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class CanvasFuncClear extends FuncType {
    public CanvasFuncClear() {
        super("clear", Arrays.asList("c"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 1, "canvas clear:invalid args size");
        Assert.require(a.get(0).v instanceof JavaType, "canvas clear:invalid arg0 type");
        Assert.require(((JavaType<?>) a.get(0).v).value instanceof QWindow, "canvas clear: invalid arg0 type");
        ((QWindow) ((JavaType<?>) a.get(0).v).value).canvas.drawings.clear();
        return new QValue();
    }

    @Override
    public QType copy() {
        return new CanvasFuncClear();
    }
}
