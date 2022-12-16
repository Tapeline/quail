package me.tapeline.quailj.runtime.std.qml.screen.window;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.std.qml.screen.window.keyboard.*;
import me.tapeline.quailj.runtime.std.qml.screen.window.mouse.*;
import me.tapeline.quailj.typing.objects.QJavaAdapter;
import me.tapeline.quailj.typing.objects.QObject;

import java.awt.*;
import java.util.HashMap;

public class QMLWindow extends QJavaAdapter<Frame> {

    public static QObject prototype = null;

    public static QObject definition(Runtime runtime) {
        if (QMLWindow.prototype == null) {
            QMLWindow.prototype = new QObject("Window", null, new HashMap<>());
            QMLWindow.prototype.setPrototypeFlag(true);
            QMLWindow.prototype.setSafeSuperClass(QJavaAdapter.definition(runtime));
            QMLWindow.prototype.table.putAll(QJavaAdapter.prototype.table);
            QMLWindow.prototype.isInheritable = false;

            QMLWindow.prototype.set("_constructor", new WindowConstructor(runtime));
            QMLWindow.prototype.set("close", new WindowFuncClose(runtime));
            QMLWindow.prototype.set("getHeight", new WindowFuncGetHeight(runtime));
            QMLWindow.prototype.set("getWidth", new WindowFuncGetWidth(runtime));
            QMLWindow.prototype.set("getX", new WindowFuncGetX(runtime));
            QMLWindow.prototype.set("getY", new WindowFuncGetY(runtime));
            QMLWindow.prototype.set("isFocused", new WindowFuncIsFocused(runtime));
            QMLWindow.prototype.set("isVisible", new WindowFuncIsVisible(runtime));
            QMLWindow.prototype.set("setPos", new WindowFuncSetPos(runtime));
            QMLWindow.prototype.set("setResizable", new WindowFuncSetResizable(runtime));
            QMLWindow.prototype.set("setSize", new WindowFuncSetSize(runtime));
            QMLWindow.prototype.set("setVisible", new WindowFuncSetVisible(runtime));
            QMLWindow.prototype.set("stampSurface", new WindowFuncStampSurface(runtime));

            QMLWindow.prototype.set("mouseAbsX", new WindowFuncMouseAbsX(runtime));
            QMLWindow.prototype.set("mouseAbsY", new WindowFuncMouseAbsY(runtime));
            QMLWindow.prototype.set("mouseX", new WindowFuncMouseX(runtime));
            QMLWindow.prototype.set("mouseY", new WindowFuncMouseY(runtime));
            QMLWindow.prototype.set("mouseButton", new WindowFuncMouseButton(runtime));
            QMLWindow.prototype.set("mousePressed", new WindowFuncMousePressed(runtime));

            QMLWindow.prototype.set("keyboardAnyPressed", new WindowFuncKeyboardAnyPressed(runtime));
            QMLWindow.prototype.set("keyboardGetPressed", new WindowFuncKeyboardGetPressed(runtime));
            QMLWindow.prototype.set("keyboardGetPressedCode", new WindowFuncKeyboardGetPressedCode(runtime));
            QMLWindow.prototype.set("keyboardGetPressedKeys", new WindowFuncKeyboardGetPressedKeys(runtime));
            QMLWindow.prototype.set("keyboardIsAlt", new WindowFuncKeyboardIsAlt(runtime));
            QMLWindow.prototype.set("keyboardIsCtrl", new WindowFuncKeyboardIsCtrl(runtime));
            QMLWindow.prototype.set("keyboardIsShift", new WindowFuncKeyboardIsShift(runtime));
        }
        return QMLWindow.prototype;
    }

    public Frame frame;
    public QCanvas canvas;
    public QMLWindowMouseHandler mouseHandler;
    public QMLWindowKeyboardHandler keyboardHandler;

    public QMLWindow(Runtime runtime, Frame object) {
        super(runtime, object);
        table.putAll(Runtime.superObject.table);
        table.putAll(definition(runtime).table);
        definition(runtime).derivedObjects.add(this);
        setObjectMetadata(definition(runtime));
        this.frame = object;
        this.canvas = new QCanvas(object.getWidth(), object.getHeight());
        frame.add(canvas);
        mouseHandler = new QMLWindowMouseHandler(runtime);
        keyboardHandler = new QMLWindowKeyboardHandler(runtime);
        frame.addKeyListener(keyboardHandler);
        canvas.addKeyListener(keyboardHandler);
        frame.addMouseListener(mouseHandler);
        canvas.addMouseListener(mouseHandler);
    }

}
