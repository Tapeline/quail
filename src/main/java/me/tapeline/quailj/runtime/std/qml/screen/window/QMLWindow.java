package me.tapeline.quailj.runtime.std.qml.screen.window;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.runtime.std.javaadapter.AdapterFuncJavaCall;
import me.tapeline.quailj.typing.objects.QJavaAdapter;
import me.tapeline.quailj.typing.objects.QObject;

import javax.swing.*;
import java.util.HashMap;

public class QMLWindow extends QJavaAdapter<JFrame> {

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
        }
        return QMLWindow.prototype;
    }

    public JFrame frame;

    public QMLWindow(Runtime runtime, JFrame object) {
        super(runtime, object);
        table.putAll(Runtime.superObject.table);
        table.putAll(definition(runtime).table);
        definition(runtime).derivedObjects.add(this);
        setObjectMetadata(definition(runtime));
        this.frame = object;
    }

}
