package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class CanvasFuncImage extends FuncType {
    public CanvasFuncImage() {
        super("image", Arrays.asList("c", "x", "y", "i"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 4, "canvas image:invalid args size");
        Assert.require(a.get(0) instanceof JavaType, "canvas image:invalid arg0 type");
        Assert.require(a.get(1) instanceof NumType, "canvas image:invalid arg1 type");
        Assert.require(a.get(2) instanceof NumType, "canvas image:invalid arg2 type");
        Assert.require(a.get(3) instanceof JavaType, "canvas image:invalid arg3 type");
        Assert.require(((JavaType<?>) a.get(0)).value instanceof QWindow, "canvas image: invalid arg0 type");
        Assert.require(((JavaType<?>) a.get(3)).value instanceof Image, "canvas image: invalid arg1 type");
        QWindow w = ((QWindow) ((JavaType<?>) a.get(0)).value);
        w.graphics().drawImage(((Image) ((JavaType<?>) a.get(3)).value),
                ((int) ((NumType) a.get(1)).value),
                ((int) ((NumType) a.get(1)).value),
                ((Image) ((JavaType<?>) a.get(3)).value).getWidth(null),
                ((Image) ((JavaType<?>) a.get(3)).value).getHeight(null),
                null
        );
        return QType.V();
    }

    @Override
    public QType copy() {
        return new CanvasFuncImage();
    }
}
