package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class CanvasFuncNewcanvas extends FuncType {
    public CanvasFuncNewcanvas() {
        super("newcanvas", Arrays.asList("name", "w", "h"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 3, "canvas newcanvas:invalid args size");
        Assert.require(a.get(0).v instanceof StringType, "canvas newcanvas:invalid arg0 type");
        Assert.require(a.get(1).v instanceof NumType, "canvas newcanvas:invalid arg1 type");
        Assert.require(a.get(2).v instanceof NumType, "canvas newcanvas:invalid arg2 type");
        QCanvas canvas = new QCanvas((int) ((NumType) a.get(1).v).value, (int) ((NumType) a.get(2).v).value);
        QWindow window = new QWindow(((StringType) a.get(0).v).value, canvas);
        JavaType<QWindow> val = new JavaType<>(window);
        val.table.put("clear", new QValue(new CanvasFuncClear()));
        val.table.put("pixel", new QValue(new CanvasFuncPixel()));
        val.table.put("line", new QValue(new CanvasFuncLine()));
        val.table.put("update", new QValue(new CanvasFuncUpdate()));
        val.table.put("text", new QValue(new CanvasFuncText()));
        val.table.put("poly", new QValue(new CanvasFuncPoly()));
        val.table.put("mouse", new QValue(new CanvasFuncMouse()));
        return new QValue(val);
    }

    @Override
    public QType copy() {
        return new CanvasFuncNewcanvas();
    }
}
