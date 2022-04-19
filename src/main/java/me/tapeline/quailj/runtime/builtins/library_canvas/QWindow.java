package me.tapeline.quailj.runtime.builtins.library_canvas;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class QWindow {

    public Frame f;
    public QCanvas canvas;

    public QWindow(String name, QCanvas c) {
        f = new Frame(name);
        canvas = c;
        f.add(canvas);
        f.setLayout(null);
        f.setSize(canvas.getWidth(), canvas.getHeight());
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing (WindowEvent e) {
                f.dispose();
            }
        });
        f.setVisible(true);
    }

    public void close() {
        f.dispose();
    }
}
