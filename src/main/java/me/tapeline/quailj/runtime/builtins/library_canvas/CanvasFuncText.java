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
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 8, "canvas text:invalid args size");
        Assert.require(a.get(0) instanceof JavaType, "canvas text:invalid arg0 type");
        Assert.require(a.get(1) instanceof StringType, "canvas text:invalid arg1 type");
        Assert.require(a.get(2) instanceof ContainerType, "canvas text:invalid arg2 type");
        Assert.require(a.get(3) instanceof NumType, "canvas text:invalid arg3 type");
        Assert.require(a.get(4) instanceof NumType, "canvas text:invalid arg4 type");
        Assert.require(a.get(5) instanceof NumType, "canvas text:invalid arg5 type");
        Assert.require(a.get(6) instanceof NumType, "canvas text:invalid arg6 type");
        Assert.require(a.get(7) instanceof NumType, "canvas text:invalid arg7 type");
        Assert.require(QType.isStr(a.get(2).table.get("font")), "canvas text:invalid font");
        Assert.require(QType.isNum(a.get(2).table.get("size")), "canvas text:invalid font");
        ((QWindow) ((JavaType<?>) a.get(0)).value).canvas.text.add(new String[] {
                a.get(2).table.get("font").toString(),
                a.get(2).table.get("size").toString(),
                ((StringType) a.get(1)).value
        });
        ((QWindow) ((JavaType<?>) a.get(0)).value).canvas.drawings.add(new double[] {
                QCanvas.DR_TEXT,
                ((NumType) a.get(5)).value,
                ((NumType) a.get(6)).value,
                ((NumType) a.get(7)).value,
                ((NumType) a.get(3)).value,
                ((NumType) a.get(4)).value,
                ((QWindow) ((JavaType<?>) a.get(0)).value).canvas.text.size() - 1
        });
        return new VoidType();
    }

    @Override
    public QType copy() {
        return new CanvasFuncText();
    }
}
