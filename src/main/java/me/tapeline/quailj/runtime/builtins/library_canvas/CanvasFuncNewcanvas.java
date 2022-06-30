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
        val.table.put(runtime, "clear",      new CanvasFuncClear());
        val.table.put(runtime, "pixel",      new CanvasFuncPixel());
        val.table.put(runtime, "line",       new CanvasFuncLine());
        val.table.put(runtime, "update",     new CanvasFuncUpdate());
        val.table.put(runtime, "text",       new CanvasFuncText());
        val.table.put(runtime, "poly",       new CanvasFuncPoly());
        val.table.put(runtime, "mouse",      new CanvasFuncMouse());
        val.table.put(runtime, "keyboard",   new CanvasFuncKeyboard());
        val.table.put(runtime, "oval",       new CanvasFuncOval());
        val.table.put(runtime, "image",      new CanvasFuncImage());
        val.table.put(runtime, "rect",       new CanvasFuncRect());
        val.table.put(runtime, "alive",      new CanvasFuncAlive());
        return val;
    }

    @Override
    public QType copy() {
        return new CanvasFuncNewcanvas();
    }
}
