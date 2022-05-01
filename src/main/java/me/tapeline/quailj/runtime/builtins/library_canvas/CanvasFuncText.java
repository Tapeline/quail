package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.util.Arrays;
import java.util.List;

public class CanvasFuncText extends FuncType {
    public CanvasFuncText() {
        super("text", Arrays.asList("c", "text", "font", "x", "y", "r", "g", "b"), null);
    }

    @Override
    public QValue run(Runtime runtime, List<QValue> a) throws RuntimeStriker {
        Assert.size(a, 8, "canvas text:invalid args size");
        Assert.require(a.get(0).v instanceof JavaType, "canvas text:invalid arg0 type");
        Assert.require(a.get(1).v instanceof StringType, "canvas text:invalid arg1 type");
        Assert.require(a.get(2).v instanceof ContainerType, "canvas text:invalid arg2 type");
        Assert.require(a.get(3).v instanceof NumType, "canvas text:invalid arg3 type");
        Assert.require(a.get(4).v instanceof NumType, "canvas text:invalid arg4 type");
        Assert.require(a.get(5).v instanceof NumType, "canvas text:invalid arg5 type");
        Assert.require(a.get(6).v instanceof NumType, "canvas text:invalid arg6 type");
        Assert.require(a.get(7).v instanceof NumType, "canvas text:invalid arg7 type");
        Assert.require(QType.isStr(a.get(2).v.table.get("font").v), "canvas text:invalid font");
        Assert.require(QType.isNum(a.get(2).v.table.get("size").v), "canvas text:invalid font");
        ((QWindow) ((JavaType<?>) a.get(0).v).value).canvas.text.add(new String[] {
                a.get(2).v.table.get("font").toString(),
                a.get(2).v.table.get("size").toString(),
                ((StringType) a.get(1).v).value
        });
        ((QWindow) ((JavaType<?>) a.get(0).v).value).canvas.drawings.add(new short[] {
                (short) QCanvas.DR_TEXT,
                (short) ((NumType) a.get(5).v).value,
                (short) ((NumType) a.get(6).v).value,
                (short) ((NumType) a.get(7).v).value,
                (short) ((NumType) a.get(3).v).value,
                (short) ((NumType) a.get(4).v).value,
                (short) ((short) ((QWindow) ((JavaType<?>) a.get(0).v).value).canvas.text.size() - 1)
        });
        QWindow win = (QWindow) ((JavaType<?>) a.get(0).v).value;
        QType flag = QValue.nullSafe(a.get(0).v.table.get("autodraw")).v;
        if (flag instanceof BoolType && ((BoolType) flag).value)
            win.canvas.paint(win.canvas.getGraphics());
        return new QValue();
    }

    @Override
    public QType copy() {
        return new CanvasFuncText();
    }
}
