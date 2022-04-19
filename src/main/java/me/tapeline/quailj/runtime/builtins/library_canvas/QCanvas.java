package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.types.ContainerType;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QCanvas extends Canvas {

    public List<double[]> drawings = new ArrayList<>();
    public List<String[]> text = new ArrayList<>();

    public static final int DR_LINE = 0;
    public static final int DR_PIXEL = 1;
    public static final int DR_TEXT = 2;
    public static final int DR_POLYGON = 3;
    public static final int DR_RECT = 4;

    public QCanvas(int w, int h) {
        setBackground(Color.WHITE);
        setSize(w, h);
    }

    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        List<double[]> current = new ArrayList<>(drawings);
        for (double[] drawing : current) {
            switch ((int) drawing[0]) {
                case DR_LINE: {
                    float[] hsb = Color.RGBtoHSB((int) drawing[1], (int) drawing[2], (int) drawing[3], null);
                    Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                    int[] a = new int[] {(int) drawing[4], (int) drawing[5]};
                    int[] b = new int[] {(int) drawing[6], (int) drawing[7]};
                    g.setColor(c);
                    g.drawLine(a[0], a[1], b[0], b[1]);
                    break;
                }
                case DR_PIXEL: {
                    float[] hsb = Color.RGBtoHSB((int) drawing[1], (int) drawing[2], (int) drawing[3], null);
                    Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                    int[] xy = new int[] {(int) drawing[4], (int) drawing[5]};
                    g.setColor(c);
                    g.drawLine(xy[0], xy[1], xy[0] + 1, xy[1] + 1);
                    break;
                }
                case DR_TEXT: {
                    float[] hsb = Color.RGBtoHSB((int) drawing[1], (int) drawing[2], (int) drawing[3], null);
                    Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                    int[] xy = new int[] {(int) drawing[4], (int) drawing[5]};
                    g.setColor(c);
                    String[] t = text.get(((int) drawing[6]));
                    Font font = new Font(t[0], Font.PLAIN, Integer.parseInt(t[1]));
                    g.setFont(font);
                    g.drawString(t[2], xy[0], xy[1]);
                    break;
                }
                case DR_POLYGON: {
                    float[] hsb = Color.RGBtoHSB((int) drawing[1], (int) drawing[2], (int) drawing[3], null);
                    Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                    int pointCount = (int) drawing[4];
                    int[] xs = new int[pointCount];
                    int[] ys = new int[pointCount];
                    for (int i = 0; i < pointCount; i += 2) {
                        xs[i] = (int) drawing[5 + i];
                        ys[i] = (int) drawing[6 + i];
                    }
                    g.setColor(c);
                    g.drawPolygon(xs, ys, pointCount);
                    break;
                }
                case DR_RECT: {
                    break;
                }
            }
        }
    }
}
