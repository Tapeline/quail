package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class CanvasFuncRect extends FuncType {
    public CanvasFuncRect() {
        super("rect", Arrays.asList("c", "x", "y", "w", "h", "fill", "r", "g", "b"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 6, "canvas rect:invalid args size");
        Assert.require(a.get(0) instanceof JavaType, "canvas rect:invalid arg0 type");
        Assert.require(a.get(1) instanceof NumType, "canvas rect:invalid arg1 type");
        Assert.require(a.get(2) instanceof NumType, "canvas rect:invalid arg2 type");
        Assert.require(a.get(3) instanceof NumType, "canvas rect:invalid arg3 type");
        Assert.require(a.get(4) instanceof NumType, "canvas rect:invalid arg4 type");
        Assert.require(a.get(5) instanceof BoolType, "canvas rect:invalid arg5 type");
        Assert.require(a.get(6) instanceof NumType, "canvas rect:invalid arg6 type");
        Assert.require(a.get(7) instanceof NumType, "canvas rect:invalid arg7 type");
        Assert.require(a.get(8) instanceof NumType, "canvas rect:invalid arg8 type");
        Assert.require(((JavaType<?>) a.get(0)).value instanceof QWindow, "canvas clear: invalid arg0 type");
        ((QWindow) ((JavaType<?>) a.get(0)).value).canvas.drawings.add(new short[] {
                QCanvas.DR_RECT,
                (short) ((NumType) a.get(6)).value,
                (short) ((NumType) a.get(7)).value,
                (short) ((NumType) a.get(8)).value,
                (short) (((BoolType) a.get(5)).value? 1 : 0),
                (short) ((NumType) a.get(1)).value,
                (short) ((NumType) a.get(2)).value,
                (short) ((NumType) a.get(3)).value,
                (short) ((NumType) a.get(4)).value,
        });
        QWindow win = (QWindow) ((JavaType<?>) a.get(0)).value;
        QType flag = QType.nullSafe(a.get(0).table.get("autodraw"));
        if (flag instanceof BoolType && ((BoolType) flag).value)
            win.canvas.paint(win.canvas.getGraphics());
        return QType.V();
    }

    @Override
    public QType copy() {
        return new CanvasFuncRect();
    }
}
