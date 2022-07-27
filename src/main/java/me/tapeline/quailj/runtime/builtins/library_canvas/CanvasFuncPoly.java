package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CanvasFuncPoly extends FuncType {
    public CanvasFuncPoly() {
        super("poly", Arrays.asList("c", "points", "r", "g", "b"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 5, "canvas poly:invalid args size");
        Assert.require(a.get(0) instanceof JavaType, "canvas poly:invalid arg0 type");
        Assert.require(a.get(1) instanceof ListType, "canvas poly:invalid arg1 type");
        Assert.require(a.get(2) instanceof NumType, "canvas poly:invalid arg2 type");
        Assert.require(a.get(3) instanceof NumType, "canvas poly:invalid arg3 type");
        Assert.require(a.get(4) instanceof NumType, "canvas poly:invalid arg4 type");
        int[] xP = new int[((ListType) a.get(1)).values.size()];
        int[] yP = new int[((ListType) a.get(1)).values.size()];
        for (int i = 0; i < ((ListType) a.get(1)).values.size(); i++) {
            QType q = ((ListType) a.get(1)).values.get(i);
            Assert.require(q instanceof ListType, "canvas poly:invalid point");
            QType x = ((ListType) q).values.get(0);
            QType y = ((ListType) q).values.get(1);
            Assert.require(QType.isNum(x, y), "canvas poly:invalid point");
            xP[i] = (int) ((NumType) x).value;
            yP[i] = (int) ((NumType) y).value;
        }
        QWindow w = ((QWindow) ((JavaType<?>) a.get(0)).value);
        Color c = new Color(
                ((int) ((NumType) a.get(5)).value),
                ((int) ((NumType) a.get(6)).value),
                ((int) ((NumType) a.get(7)).value)
        );
        w.graphics().setColor(c);
        w.graphics().drawPolygon(
                xP, yP, ((ListType) a.get(1)).values.size()
        );
        return QType.V();
    }

    @Override
    public QType copy() {
        return new CanvasFuncPoly();
    }
}
