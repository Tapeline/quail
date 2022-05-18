package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class CanvasFuncUpdate extends FuncType {
    public CanvasFuncUpdate() {
        super("update", Arrays.asList("c"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 1, "canvas update:invalid args size");
        Assert.require(a.get(0) instanceof JavaType, "canvas update:invalid arg0 type");
        Assert.require(((JavaType<?>) a.get(0)).value instanceof QWindow, "canvas update:invalid arg0 type");
        QCanvas canvas = ((QWindow) ((JavaType<?>) a.get(0)).value).canvas;
        canvas.paint(canvas.getGraphics());
        return QType.V();
    }

    @Override
    public QType copy() {
        return new CanvasFuncUpdate();
    }
}
