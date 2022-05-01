package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class CanvasFuncPixel extends FuncType {
    public CanvasFuncPixel() {
        super("pixel", Arrays.asList("c", "x", "y", "r", "g", "b"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 6, "canvas pixel:invalid args size");
        Assert.require(a.get(0).v instanceof JavaType, "canvas pixel:invalid arg0 type");
        Assert.require(a.get(1).v instanceof NumType, "canvas pixel:invalid arg1 type");
        Assert.require(a.get(2).v instanceof NumType, "canvas pixel:invalid arg2 type");
        Assert.require(a.get(3).v instanceof NumType, "canvas pixel:invalid arg3 type");
        Assert.require(a.get(4).v instanceof NumType, "canvas pixel:invalid arg4 type");
        Assert.require(a.get(5).v instanceof NumType, "canvas pixel:invalid arg5 type");
        Assert.require(((JavaType<?>) a.get(0).v).value instanceof QWindow, "canvas clear: invalid arg0 type");
        QWindow win = (QWindow) ((JavaType<?>) a.get(0).v).value;
        short[] drawing = new short[] {
                QCanvas.DR_PIXEL,
                (short) ((NumType) a.get(3).v).value,
                (short) ((NumType) a.get(4).v).value,
                (short) ((NumType) a.get(5).v).value,
                (short) ((NumType) a.get(1).v).value,
                (short) ((NumType) a.get(2).v).value,
        };
        if (!win.canvas.containsDrawing(drawing))
            ((QWindow) ((JavaType<?>) a.get(0).v).value).canvas.drawings.add(drawing);
        QType flag = a.get(0).v.table.get("autodraw").v;
        if (flag instanceof BoolType && ((BoolType) flag).value)
            win.canvas.paint(win.canvas.getGraphics());
        return new QValue();
    }

    @Override
    public QType copy() {
        return new CanvasFuncPixel();
    }
}
