package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class CanvasFuncOval extends FuncType {
    public CanvasFuncOval() {
        super("oval", Arrays.asList(
                "c", "x1", "y1", "x2", "y2", "fill", "r", "g", "b"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 9, "canvas oval:invalid args size");
        Assert.require(a.get(0) instanceof JavaType, "canvas oval:invalid arg0 type");
        Assert.require(a.get(1) instanceof NumType, "canvas oval:invalid arg1 type");
        Assert.require(a.get(2) instanceof NumType, "canvas oval:invalid arg2 type");
        Assert.require(a.get(3) instanceof NumType, "canvas oval:invalid arg3 type");
        Assert.require(a.get(4) instanceof NumType, "canvas oval:invalid arg4 type");
        Assert.require(a.get(5) instanceof BoolType, "canvas oval:invalid arg6 type");
        Assert.require(a.get(6) instanceof NumType, "canvas oval:invalid arg7 type");
        Assert.require(a.get(7) instanceof NumType, "canvas oval:invalid arg7 type");
        Assert.require(a.get(8) instanceof NumType, "canvas oval:invalid arg7 type");
        Assert.require(((JavaType<?>) a.get(0)).value instanceof QWindow, "canvas clear: invalid arg0 type");
        int r = ((int) ((NumType) a.get(6)).value);
        int g = ((int) ((NumType) a.get(7)).value);
        int b = ((int) ((NumType) a.get(8)).value);
        int c1 = ((int) ((NumType) a.get(1)).value);
        int c2 = ((int) ((NumType) a.get(2)).value);
        int c3 = ((int) ((NumType) a.get(3)).value);
        int c4 = ((int) ((NumType) a.get(4)).value);
        boolean fill = ((BoolType) a.get(5)).value;

        QWindow w = ((QWindow) ((JavaType<?>) a.get(0)).value);
        Graphics2D graphics = w.graphics();
        graphics.setColor(new Color(r, g, b));
        if (fill) {
            graphics.fillOval(c1, c2, c3, c4);
        } else {
            graphics.drawOval(c1, c2, c3, c4);
        }
        return QType.V();
    }

    @Override
    public QType copy() {
        return new CanvasFuncOval();
    }
}
