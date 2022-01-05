package me.tapeline.quailj.debugtools;

import me.tapeline.quailj.parsingtools.nodes.*;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

public class NodeTreeFrame extends JFrame {

    private final Node root;

    public NodeTreeFrame(Node node) {
        super("QuailJ Node Tree Viewer");
        this.root = node;
        setPreferredSize(new Dimension(600, 400));
        setSize(600, 400);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JTree tree = new JTree(root.toTree());
        JScrollPane scrollPane = new JScrollPane();
        Renderer renderer = new Renderer();
        tree.setCellRenderer(renderer);
        scrollPane.getViewport().add(tree);
        getContentPane().add("Center", scrollPane);
        setVisible(true);
    }
}

class Renderer extends JLabel implements TreeCellRenderer {
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
                                                  boolean expanded, boolean leaf, int row, boolean hasFocus) {
        setText(value.toString() + "                   ");
        return this;
    }
}