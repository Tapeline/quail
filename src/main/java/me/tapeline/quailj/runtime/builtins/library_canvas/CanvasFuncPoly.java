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
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 5, "canvas poly:invalid args size");
        Assert.require(a.get(0).v instanceof JavaType, "canvas poly:invalid arg0 type");
        Assert.require(a.get(1).v instanceof ListType, "canvas poly:invalid arg1 type");
        Assert.require(a.get(2).v instanceof NumType, "canvas poly:invalid arg2 type");
        Assert.require(a.get(3).v instanceof NumType, "canvas poly:invalid arg3 type");
        Assert.require(a.get(4).v instanceof NumType, "canvas poly:invalid arg4 type");
        List<Double> d = new ArrayList<>();
        d.add((double) QCanvas.DR_POLYGON);
        d.add(((NumType) a.get(2).v).value);
        d.add(((NumType) a.get(3).v).value);
        d.add(((NumType) a.get(4).v).value);
        d.add((double) ((ListType) a.get(1).v).values.size());
        for (QValue q : ((ListType) a.get(1).v).values) {
            Assert.require(q.v instanceof ListType, "canvas poly:invalid point");
            QType x = ((ListType) q.v).values.get(0).v;
            QType y = ((ListType) q.v).values.get(1).v;
            Assert.require(QType.isNum(x, y), "canvas poly:invalid point");
            d.add(((NumType) x).value);
            d.add(((NumType) y).value);
        }
        Assert.require(((JavaType<?>) a.get(0).v).value instanceof QWindow, "canvas poly: invalid arg0 type");
        short[] arr = new short[d.size()];
        for (int i = 0; i < d.size(); i++) arr[i] = d.get(i).shortValue();
        ((QWindow) ((JavaType<?>) a.get(0).v).value).canvas.drawings.add(arr);
        QWindow win = (QWindow) ((JavaType<?>) a.get(0).v).value;
        QType flag = a.get(0).v.table.get("autodraw").v;
        if (flag instanceof BoolType && ((BoolType) flag).value)
            win.canvas.paint(win.canvas.getGraphics());
        return new QValue();
    }

    @Override
    public QType copy() {
        return new CanvasFuncPoly();
    }
}
