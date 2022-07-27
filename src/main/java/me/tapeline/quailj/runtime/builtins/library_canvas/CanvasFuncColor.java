package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class CanvasFuncColor extends FuncType {
    public CanvasFuncColor() {
        super("color", Arrays.asList("c", "r", "g", "b"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 4, "canvas color:invalid args size");
        Assert.require(a.get(0) instanceof JavaType, "canvas color:invalid arg0 type");
        Assert.require(a.get(1) instanceof NumType, "canvas color:invalid arg1 type");
        Assert.require(a.get(2) instanceof NumType, "canvas color:invalid arg2 type");
        Assert.require(a.get(3) instanceof NumType, "canvas color:invalid arg3 type");
        Assert.require(((JavaType<?>) a.get(0)).value instanceof QWindow, "canvas color: invalid arg0 type");
        QWindow w = ((QWindow) ((JavaType<?>) a.get(0)).value);
        w.graphics().setColor(new Color(
                ((int) ((NumType) a.get(1)).value),
                ((int) ((NumType) a.get(2)).value),
                ((int) ((NumType) a.get(3)).value)
        ));
        return QType.V();
    }

    @Override
    public QType copy() {
        return new CanvasFuncColor();
    }
}
