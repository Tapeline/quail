package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class CanvasFuncLine extends FuncType {
    public CanvasFuncLine() {
        super("line", Arrays.asList("c", "x1", "y1", "x2", "y2", "r", "g", "b"), null);
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
        Assert.require(a.get(6).v instanceof NumType, "canvas pixel:invalid arg6 type");
        Assert.require(a.get(7).v instanceof NumType, "canvas pixel:invalid arg7 type");
        Assert.require(((JavaType<?>) a.get(0).v).value instanceof QWindow, "canvas clear: invalid arg0 type");
        ((QWindow) ((JavaType<?>) a.get(0).v).value).canvas.drawings.add(new short[] {
                QCanvas.DR_LINE,
                (short) ((NumType) a.get(5).v).value,
                (short) ((NumType) a.get(6).v).value,
                (short) ((NumType) a.get(7).v).value,
                (short) ((NumType) a.get(1).v).value,
                (short) ((NumType) a.get(2).v).value,
                (short) ((NumType) a.get(3).v).value,
                (short) ((NumType) a.get(4).v).value,
        });
        QWindow win = (QWindow) ((JavaType<?>) a.get(0).v).value;
        QType flag = QValue.nullSafe(a.get(0).v.table.get("autodraw")).v;
        if (flag instanceof BoolType && ((BoolType) flag).value)
            win.canvas.paint(win.canvas.getGraphics());
        return new QValue();
    }

    @Override
    public QType copy() {
        return new CanvasFuncLine();
    }
}
