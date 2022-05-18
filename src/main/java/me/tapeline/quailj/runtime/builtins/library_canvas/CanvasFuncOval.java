package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class CanvasFuncOval extends FuncType {
    public CanvasFuncOval() {
        super("oval", Arrays.asList("c", "x1", "y1", "x2", "y2", "fill", "r", "g", "b"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 6, "canvas oval:invalid args size");
        Assert.require(a.get(0) instanceof JavaType, "canvas oval:invalid arg0 type");
        Assert.require(a.get(1) instanceof NumType, "canvas oval:invalid arg1 type");
        Assert.require(a.get(2) instanceof NumType, "canvas oval:invalid arg2 type");
        Assert.require(a.get(3) instanceof NumType, "canvas oval:invalid arg3 type");
        Assert.require(a.get(4) instanceof NumType, "canvas oval:invalid arg4 type");
        Assert.require(a.get(5) instanceof NumType, "canvas oval:invalid arg5 type");
        Assert.require(a.get(6) instanceof BoolType, "canvas oval:invalid arg6 type");
        Assert.require(a.get(7) instanceof NumType, "canvas oval:invalid arg7 type");
        Assert.require(a.get(8) instanceof NumType, "canvas oval:invalid arg7 type");
        Assert.require(((JavaType<?>) a.get(0)).value instanceof QWindow, "canvas clear: invalid arg0 type");
        ((QWindow) ((JavaType<?>) a.get(0)).value).canvas.drawings.add(new short[] {
                QCanvas.DR_OVAL,
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
        return new CanvasFuncOval();
    }
}
