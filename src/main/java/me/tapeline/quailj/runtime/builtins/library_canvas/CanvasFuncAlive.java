package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class CanvasFuncAlive extends FuncType {
    public CanvasFuncAlive() {
        super("alive", Arrays.asList("c"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 1, "canvas alive:invalid args size");
        Assert.require(a.get(0) instanceof JavaType, "canvas alive:invalid arg0 type");
        Assert.require(((JavaType<?>) a.get(0)).value instanceof QWindow, "canvas clear: invalid arg0 type");
        return QType.V(((QWindow) ((JavaType<?>) a.get(0)).value).f.isShowing());
    }

    @Override
    public QType copy() {
        return new CanvasFuncAlive();
    }
}
