package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

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
        List<Double> d = new ArrayList<>();
        d.add((double) QCanvas.DR_POLYGON);
        d.add(((NumType) a.get(2)).value);
        d.add(((NumType) a.get(3)).value);
        d.add(((NumType) a.get(4)).value);
        d.add((double) ((ListType) a.get(1)).values.size());
        for (QType q : ((ListType) a.get(1)).values) {
            Assert.require(q instanceof ListType, "canvas poly:invalid point");
            QType x = ((ListType) q).values.get(0);
            QType y = ((ListType) q).values.get(1);
            Assert.require(QType.isNum(x, y), "canvas poly:invalid point");
            d.add(((NumType) x).value);
            d.add(((NumType) y).value);
        }
        Assert.require(((JavaType<?>) a.get(0)).value instanceof QWindow, "canvas poly: invalid arg0 type");
        double[] arr = new double[d.size()];
        for (int i = 0; i < d.size(); i++) arr[i] = d.get(i);
        ((QWindow) ((JavaType<?>) a.get(0)).value).canvas.drawings.add(arr);
        return new VoidType();
    }

    @Override
    public QType copy() {
        return new CanvasFuncPoly();
    }
}
