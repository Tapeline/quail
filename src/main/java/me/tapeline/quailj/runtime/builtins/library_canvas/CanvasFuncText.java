package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.awt.*;
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
        Assert.require(QType.isNum(a.get(2).table.get("style")), "canvas text:invalid font");
        QWindow w = ((QWindow) ((JavaType<?>) a.get(0)).value);
        int r = ((int) ((NumType) a.get(5)).value);
        int g = ((int) ((NumType) a.get(6)).value);
        int b = ((int) ((NumType) a.get(7)).value);
        int c1 = ((int) ((NumType) a.get(3)).value);
        int c2 = ((int) ((NumType) a.get(4)).value);
        Font font = new Font(
                a.get(2).table.get("font").toString(),
                ((int) ((NumType) a.get(2).table.get("style")).value),
                (int) ((NumType) a.get(2).table.get("size")).value
        );

        Graphics2D graphics = w.graphics();
        graphics.setColor(new Color(r, g, b));
        graphics.setFont(font);
        graphics.drawString(a.get(1).toString(), c1, c2);
        return QType.V();
    }

    @Override
    public QType copy() {
        return new CanvasFuncText();
    }
}
