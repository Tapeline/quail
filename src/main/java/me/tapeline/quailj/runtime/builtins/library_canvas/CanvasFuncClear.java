package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class CanvasFuncClear extends FuncType {
    public CanvasFuncClear() {
        super("clear", Arrays.asList("c"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 1, "canvas clear:invalid args size");
        Assert.require(a.get(0) instanceof JavaType, "canvas clear:invalid arg0 type");
        Assert.require(((JavaType<?>) a.get(0)).value instanceof QWindow, "canvas clear: invalid arg0 type");
        QWindow w = ((QWindow) ((JavaType<?>) a.get(0)).value);
        w.graphics().setColor(new Color(0xFFFFFF));
        w.graphics().fillRect(0, 0, w.f.getWidth(), w.f.getHeight());
        return QType.V();
    }

    @Override
    public QType copy() {
        return new CanvasFuncClear();
    }
}
