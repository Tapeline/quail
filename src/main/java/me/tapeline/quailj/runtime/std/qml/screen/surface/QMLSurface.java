package me.tapeline.quailj.runtime.std.qml.screen.surface;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QJavaAdapter;
import me.tapeline.quailj.typing.objects.QObject;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

public class QMLSurface extends QJavaAdapter<BufferedImage> {

    public static QObject prototype = null;

    public static QObject definition(Runtime runtime) {
        if (QMLSurface.prototype == null) {
            QMLSurface.prototype = new QObject("Surface", null, new HashMap<>());
            QMLSurface.prototype.setPrototypeFlag(true);
            QMLSurface.prototype.setSafeSuperClass(QJavaAdapter.definition(runtime));
            QMLSurface.prototype.table.putAll(QJavaAdapter.prototype.table);
            QMLSurface.prototype.isInheritable = false;

            QMLSurface.prototype.set("_constructor", new SurfaceConstructor(runtime));
            QMLSurface.prototype.set("clear", new SurfaceFuncClear(runtime));
            QMLSurface.prototype.set("drawLine", new SurfaceFuncDrawLine(runtime));
            QMLSurface.prototype.set("drawOval", new SurfaceFuncDrawOval(runtime));
            QMLSurface.prototype.set("drawPixel", new SurfaceFuncDrawPixel(runtime));
            QMLSurface.prototype.set("drawRect", new SurfaceFuncDrawRect(runtime));
            QMLSurface.prototype.set("drawText", new SurfaceFuncDrawText(runtime));
            QMLSurface.prototype.set("fillOval", new SurfaceFuncFillOval(runtime));
            QMLSurface.prototype.set("fillRect", new SurfaceFuncFillRect(runtime));
            QMLSurface.prototype.set("getHeight", new SurfaceFuncGetHeight(runtime));
            QMLSurface.prototype.set("getWidth", new SurfaceFuncGetWidth(runtime));
            QMLSurface.prototype.set("resizeStamp", new SurfaceFuncResizeStamp(runtime));
            QMLSurface.prototype.set("setColor", new SurfaceFuncSetColor(runtime));
            QMLSurface.prototype.set("setColorHSB", new SurfaceFuncSetColorHSB(runtime));
            QMLSurface.prototype.set("setColorRGBA", new SurfaceFuncSetColorRGBA(runtime));
            QMLSurface.prototype.set("setFont", new SurfaceFuncSetFont(runtime));
            QMLSurface.prototype.set("stamp", new SurfaceFuncStamp(runtime));
            QMLSurface.prototype.set("typeText", new SurfaceFuncTypeText(runtime));
        }
        return QMLSurface.prototype;
    }

    public Graphics2D graphics;
    public BufferedImage image;

    public QMLSurface(Runtime runtime, BufferedImage image) {
        super(runtime, image);
        table.putAll(Runtime.superObject.table);
        table.putAll(definition(runtime).table);
        definition(runtime).derivedObjects.add(this);
        setObjectMetadata(definition(runtime));
        this.graphics = image.createGraphics();
        this.image = image;
    }

}
