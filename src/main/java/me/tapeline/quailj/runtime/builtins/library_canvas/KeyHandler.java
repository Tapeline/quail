package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.ContainerType;
import me.tapeline.quailj.types.QValue;
import me.tapeline.quailj.types.RuntimeStriker;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KeyHandler implements KeyListener {

    public volatile List<Character> pressed = new ArrayList<>();

    public Runtime runtime;

    public KeyHandler(Runtime r) {
        runtime = r;
    }


    @Override
    public void keyTyped(KeyEvent e) {
        HashMap<String, QValue> data = new HashMap<>();
        data.put("key", new QValue(e.getKeyChar() + ""));
        try {
            Runtime.callEvent(runtime, "canvas.keyclick", new ContainerType(data));
        } catch (RuntimeStriker ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!pressed.contains(e.getKeyChar())) pressed.add(e.getKeyChar());
        HashMap<String, QValue> data = new HashMap<>();
        data.put("key", new QValue(e.getKeyChar() + ""));
        try {
            Runtime.callEvent(runtime, "canvas.keydown", new ContainerType(data));
        } catch (RuntimeStriker ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressed.remove((Character) e.getKeyChar());
        HashMap<String, QValue> data = new HashMap<>();
        data.put("key", new QValue(e.getKeyChar() + ""));
        try {
            Runtime.callEvent(runtime, "canvas.keyup", new ContainerType(data));
        } catch (RuntimeStriker ex) {
            throw new RuntimeException(ex);
        }
    }
}
