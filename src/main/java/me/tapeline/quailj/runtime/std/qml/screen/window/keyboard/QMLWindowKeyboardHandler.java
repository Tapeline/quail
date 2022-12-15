package me.tapeline.quailj.runtime.std.qml.screen.window.keyboard;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.typing.objects.QObject;
import me.tapeline.quailj.typing.objects.errors.RuntimeStriker;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class QMLWindowKeyboardHandler implements KeyListener {

    public volatile List<Character> pressed = new ArrayList<>();
    public volatile boolean isCtrl = false;
    public volatile boolean isShift = false;
    public volatile boolean isAlt = false;
    public volatile boolean anyPressed = false;
    public volatile char lastPressed = '\0';
    public volatile int lastPressedCode = 0;

    public Runtime runtime;

    public QMLWindowKeyboardHandler(Runtime r) {
        runtime = r;
    }


    @Override
    public void keyTyped(KeyEvent e) {
        HashMap<String, QObject> data = new HashMap<>();
        data.put("key", QObject.Val(e.getKeyChar() + ""));
        try {
            runtime.callEvent("qml.keyType", data);
        } catch (RuntimeStriker ex) {
            throw new RuntimeException(ex);
        }
        isCtrl = e.isControlDown();
        isAlt = e.isAltDown();
        isShift = e.isShiftDown();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!pressed.contains(e.getKeyChar())) pressed.add(e.getKeyChar());
        HashMap<String, QObject> data = new HashMap<>();
        data.put("key", QObject.Val(e.getKeyChar() + ""));
        try {
            runtime.callEvent("qml.keyDown", data);
        } catch (RuntimeStriker ex) {
            throw new RuntimeException(ex);
        }
        isCtrl = e.isControlDown();
        isAlt = e.isAltDown();
        isShift = e.isShiftDown();
        anyPressed = true;
        lastPressed = e.getKeyChar();
        lastPressedCode = e.getKeyCode();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressed.remove((Character) e.getKeyChar());
        HashMap<String, QObject> data = new HashMap<>();
        data.put("key", QObject.Val(e.getKeyChar() + ""));
        try {
            runtime.callEvent("qml.keyUp", data);
        } catch (RuntimeStriker ex) {
            throw new RuntimeException(ex);
        }
        isCtrl = e.isControlDown();
        isAlt = e.isAltDown();
        isShift = e.isShiftDown();
        if (pressed.size() == 0)
            anyPressed = false;
    }

}
