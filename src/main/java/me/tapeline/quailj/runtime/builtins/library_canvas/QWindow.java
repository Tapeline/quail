package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class QWindow {

    public Frame f;
    public QCanvas canvas;
    public MouseHandler mouse;
    public KeyHandler keyboard;

    public QWindow(Runtime r, String name, int w, int h) {
        f = new Frame(name);
        canvas = new QCanvas(w, h);
        f.add(canvas);
        f.setLayout(null);
        f.setSize(w, h);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing (WindowEvent e) {
                f.dispose();
            }
        });
        mouse = new MouseHandler(r);
        f.addMouseListener(mouse);
        canvas.addMouseListener(mouse);
        keyboard = new KeyHandler(r);
        f.addKeyListener(keyboard);
        f.setVisible(true);
    }

    public void close() {
        f.dispose();
    }

    public Graphics2D graphics() {
        return (Graphics2D) canvas.getGraphics();
    }
}
