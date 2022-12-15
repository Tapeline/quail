package me.tapeline.quailj.runtime.std.qml.screen.window;

import java.awt.*;
import java.awt.image.BufferedImage;

public class QCanvas extends Canvas {

    public BufferedImage image = null;

    public QCanvas(int w, int h) {
        setBackground(Color.WHITE);
        setSize(w, h);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (image != null) {
            g.drawImage(image, 0, 0, null);
        }
    }

}
