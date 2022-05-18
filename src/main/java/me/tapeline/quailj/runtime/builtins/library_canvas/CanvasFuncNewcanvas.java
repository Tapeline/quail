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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 3, "canvas newcanvas:invalid args size");
        Assert.require(a.get(0) instanceof StringType, "canvas newcanvas:invalid arg0 type");
        Assert.require(a.get(1) instanceof NumType, "canvas newcanvas:invalid arg1 type");
        Assert.require(a.get(2) instanceof NumType, "canvas newcanvas:invalid arg2 type");
        QCanvas canvas = new QCanvas((int) ((NumType) a.get(1)).value, (int) ((NumType) a.get(2)).value);
        QWindow window = new QWindow(runtime, ((StringType) a.get(0)).value, canvas);
        JavaType<QWindow> val = new JavaType<>(window);
        val.table.put("clear",      new CanvasFuncClear());
        val.table.put("pixel",      new CanvasFuncPixel());
        val.table.put("line",       new CanvasFuncLine());
        val.table.put("update",     new CanvasFuncUpdate());
        val.table.put("text",       new CanvasFuncText());
        val.table.put("poly",       new CanvasFuncPoly());
        val.table.put("mouse",      new CanvasFuncMouse());
        val.table.put("keyboard",   new CanvasFuncKeyboard());
        val.table.put("oval",       new CanvasFuncOval());
        val.table.put("image",      new CanvasFuncImage());
        val.table.put("rect",       new CanvasFuncRect());
        val.table.put("alive",      new CanvasFuncAlive());
        return val;
    }

    @Override
    public QType copy() {
        return new CanvasFuncNewcanvas();
    }
}
