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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 1, "canvas mouse:invalid args size");
        Assert.require(a.get(0) instanceof JavaType, "canvas mouse:invalid arg0 type");
        Assert.require(((JavaType<?>) a.get(0)).value instanceof QWindow, "canvas mouse: invalid arg0 type");
        HashMap<String, QType> data = new HashMap<>();
        Point point = MouseInfo.getPointerInfo().getLocation();
        QWindow win = ((QWindow) ((JavaType<?>) a.get(0)).value);
        data.put("absX", QType.V(point.x));
        data.put("absY", QType.V(point.y));
        data.put("x", QType.V(point.x - win.f.getX()));
        data.put("y", QType.V(point.y - win.f.getY()));
        data.put("pressed", QType.V(win.mouse.mouseDown));
        data.put("button", QType.V(win.mouse.mouseBtn));
        return new ContainerType(data);
    }

    @Override
    public QType copy() {
        return new CanvasFuncMouse();
    }
}
