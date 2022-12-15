package me.tapeline.quailj.runtime.std.qml.screen.window.mouse;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class QMLWindowMouseHandler implements MouseListener {

    public volatile boolean mouseDown = false;
    public volatile int mouseBtn = -1;
    public Runtime runtime;

    public QMLWindowMouseHandler(Runtime r) {
        runtime = r;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        HashMap<String, QObject> data = new HashMap<>();
        data.put("button", QObject.Val(e.getButton()));
        try {
            runtime.callEvent("qml.mouseClick", data);
        } catch (RuntimeStriker ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = true;
        mouseBtn = e.getButton();
        HashMap<String, QObject> data = new HashMap<>();
        data.put("button", QObject.Val(mouseBtn));
        try {
            runtime.callEvent("qml.mouseDown", data);
        } catch (RuntimeStriker ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
        mouseBtn = -1;
        HashMap<String, QObject> data = new HashMap<>();
        data.put("button", QObject.Val(mouseBtn));
        try {
            runtime.callEvent("qml.mouseUp", data);
        } catch (RuntimeStriker ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}

