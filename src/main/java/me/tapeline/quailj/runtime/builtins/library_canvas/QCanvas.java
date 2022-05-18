package me.tapeline.quailj.runtime.builtins.library_canvas;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QCanvas extends Canvas {

    public List<short[]> drawings = new ArrayList<>();
    public List<String[]> text = new ArrayList<>();
    public List<Image> images = new ArrayList<>();

    public static final int DR_LINE = 0;
    public static final int DR_PIXEL = 1;
    public static final int DR_TEXT = 2;
    public static final int DR_POLYGON = 3;
    public static final int DR_RECT = 4;
    public static final int DR_OVAL = 5;
    public static final int DR_IMAGE = 6;

    public QCanvas(int w, int h) {
        setBackground(Color.WHITE);
        setSize(w, h);
    }

    public boolean containsPixel(int x, int y, int r, int g, int b) {
        for (short[] d : drawings)
            if (d[1] == r && d[2] == g && d[3] == b && d[4] == x && d[5] == y)
                return true;
        return false;
    }

    public boolean containsDrawing(short[] d) {
        for (short[] dd : drawings)
            if (Arrays.equals(dd, d))
                return true;
        return false;
    }

    public void paint(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        List<short[]> current = new ArrayList<>(drawings);
        for (short[] drawing : current) {
            switch ((int) drawing[0]) {
                case DR_LINE: {
                    float[] hsb = Color.RGBtoHSB(drawing[1], drawing[2], drawing[3], null);
                    Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                    int[] a = new int[] {(int) drawing[4], (int) drawing[5]};
                    int[] b = new int[] {(int) drawing[6], (int) drawing[7]};
                    g.setColor(c);
                    g.drawLine(a[0], a[1], b[0], b[1]);
                    break;
                }
                case DR_PIXEL: {
                    float[] hsb = Color.RGBtoHSB(drawing[1], drawing[2], drawing[3], null);
                    Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                    int[] xy = new int[] {(int) drawing[4], (int) drawing[5]};
                    g.setColor(c);
                    g.drawRect(xy[0], xy[1], 1, 1);
                    //g.drawLine(xy[0], xy[1], xy[0] + 1, xy[1] + 1);
                    break;
                }
                case DR_TEXT: {
                    float[] hsb = Color.RGBtoHSB(drawing[1], drawing[2], drawing[3], null);
                    Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                    int[] xy = new int[] {(int) drawing[4], (int) drawing[5]};
                    g.setColor(c);
                    String[] t = text.get(drawing[6]);
                    Font font = new Font(t[0], Font.PLAIN, Integer.parseInt(t[1]));
                    g.setFont(font);
                    g.drawString(t[2], xy[0], xy[1]);
                    break;
                }
                case DR_POLYGON: {
                    float[] hsb = Color.RGBtoHSB(drawing[1], drawing[2], drawing[3], null);
                    Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                    int pointCount = drawing[4];
                    int[] xs = new int[pointCount];
                    int[] ys = new int[pointCount];
                    for (int i = 0; i < pointCount; i += 2) {
                        xs[i] = drawing[5 + i];
                        ys[i] = drawing[6 + i];
                    }
                    g.setColor(c);
                    g.drawPolygon(xs, ys, pointCount);
                    break;
                }
                case DR_RECT: {
                    float[] hsb = Color.RGBtoHSB(drawing[1], drawing[2], drawing[3], null);
                    Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                    boolean doFill = drawing[4] == 1;
                    int[] a = new int[] {(int) drawing[5], (int) drawing[6]};
                    int[] b = new int[] {(int) drawing[7], (int) drawing[8]};
                    g.setColor(c);
                    if (doFill) g.fillRect(a[0], a[1], b[0], b[1]);
                    else g.drawRect(a[0], a[1], b[0], b[1]);
                    break;
                }
                case DR_OVAL: {
                    float[] hsb = Color.RGBtoHSB(drawing[1], drawing[2], drawing[3], null);
                    Color c = Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
                    boolean doFill = drawing[4] == 1;
                    int[] a = new int[] {(int) drawing[5], (int) drawing[6]};
                    int[] b = new int[] {(int) drawing[7], (int) drawing[8]};
                    g.setColor(c);
                    if (doFill) g.fillOval(a[0], a[1], b[0], b[1]);
                    else g.drawOval(a[0], a[1], b[0], b[1]);
                    break;
                }
                case DR_IMAGE: {
                    int[] xy = new int[] {(int) drawing[1], (int) drawing[2]};
                    g.drawImage(images.get(drawing[3]), xy[0], xy[1], null);
                    break;
                }
            }
        }
    }
}
