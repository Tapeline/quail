package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.awt.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CanvasFuncMouse extends FuncType {
    public CanvasFuncMouse() {
        super("mouse", Arrays.asList("c"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 1, "canvas mouse:invalid args size");
        Assert.require(a.get(0).v instanceof JavaType, "canvas mouse:invalid arg0 type");
        Assert.require(((JavaType<?>) a.get(0).v).value instanceof QWindow, "canvas mouse: invalid arg0 type");
        HashMap<String, QValue> data = new HashMap<>();
        Point point = MouseInfo.getPointerInfo().getLocation();
        QWindow win = ((QWindow) ((JavaType<?>) a.get(0).v).value);
        data.put("absX", new QValue(point.x));
        data.put("absY", new QValue(point.y));
        data.put("x", new QValue(point.x - win.f.getX()));
        data.put("y", new QValue(point.y - win.f.getY()));
        data.put("pressed", new QValue(win.mouse.mouseDown));
        return new QValue(new ContainerType(data));
    }

    @Override
    public QType copy() {
        return new CanvasFuncMouse();
    }
}
