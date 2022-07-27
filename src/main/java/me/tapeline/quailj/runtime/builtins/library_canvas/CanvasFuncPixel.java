package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class CanvasFuncPixel extends FuncType {
    public CanvasFuncPixel() {
        super("pixel", Arrays.asList("c", "x", "y", "r", "g", "b"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 6, "canvas pixel:invalid args size");
        Assert.require(a.get(0) instanceof JavaType, "canvas pixel:invalid arg0 type");
        Assert.require(a.get(1) instanceof NumType, "canvas pixel:invalid arg1 type");
        Assert.require(a.get(2) instanceof NumType, "canvas pixel:invalid arg2 type");
        Assert.require(a.get(3) instanceof NumType, "canvas pixel:invalid arg3 type");
        Assert.require(a.get(4) instanceof NumType, "canvas pixel:invalid arg4 type");
        Assert.require(a.get(5) instanceof NumType, "canvas pixel:invalid arg5 type");
        Assert.require(((JavaType<?>) a.get(0)).value instanceof QWindow, "canvas clear: invalid arg0 type");
        int r = ((int) ((NumType) a.get(3)).value);
        int g = ((int) ((NumType) a.get(4)).value);
        int b = ((int) ((NumType) a.get(5)).value);
        int c1 = ((int) ((NumType) a.get(1)).value);
        int c2 = ((int) ((NumType) a.get(2)).value);

        QWindow w = ((QWindow) ((JavaType<?>) a.get(0)).value);
        Graphics2D graphics = w.graphics();
        graphics.setColor(new Color(r, g, b));
        graphics.drawRect(c1, c2, 1, 1);
        return QType.V();
    }

    @Override
    public QType copy() {
        return new CanvasFuncPixel();
    }
}
