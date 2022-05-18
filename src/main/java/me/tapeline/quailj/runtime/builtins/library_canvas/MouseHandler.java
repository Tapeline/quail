package me.tapeline.quailj.runtime.builtins.library_canvas;

import me.tapeline.quailj.runtime.Runtime;
import me.tapeline.quailj.types.ContainerType;
import me.tapeline.quailj.types.QType;
import me.tapeline.quailj.types.RuntimeStriker;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;

public class MouseHandler implements MouseListener {

    public volatile boolean mouseDown = false;
    public volatile int mouseBtn = -1;
    public Runtime runtime;

    public MouseHandler(Runtime r) {
        runtime = r;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        HashMap<String, QType> data = new HashMap<>();
        data.put("button", QType.V(e.getButton()));
        try {
            Runtime.callEvent(runtime, "canvas.mouseclick", new ContainerType(data));
        } catch (RuntimeStriker ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mouseDown = true;
        mouseBtn = e.getButton();
        HashMap<String, QType> data = new HashMap<>();
        data.put("button", QType.V(mouseBtn));
        try {
            Runtime.callEvent(runtime, "canvas.mousedown", new ContainerType(data));
        } catch (RuntimeStriker ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        mouseDown = false;
        mouseBtn = -1;
        HashMap<String, QType> data = new HashMap<>();
        data.put("button", QType.V(mouseBtn));
        try {
            Runtime.callEvent(runtime, "canvas.mouseup", new ContainerType(data));
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
