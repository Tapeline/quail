package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.*;
import me.tapeline.quailj.utils.Assert;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CanvasFuncLoadimage extends FuncType {
    public CanvasFuncLoadimage() {
        super("loadimage", Arrays.asList("path"), null);
    }

    @Override
    public QType run(Runtime runtime, List<QType> a) throws RuntimeStriker {
        Assert.size(a, 1, "canvas loadimage:invalid args size");
        Assert.require(a.get(0) instanceof StringType, "canvas loadimage:invalid arg0 type");
        try {
            Image im = ImageIO.read(new File(a.get(0).toString()));
            return new JavaType<>(im);
        } catch (IOException e) {
            return QType.V();
        }
    }

    @Override
    public QType copy() {
        return new CanvasFuncLoadimage();
    }
}
