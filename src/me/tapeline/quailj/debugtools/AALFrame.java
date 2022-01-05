package me.tapeline.quailj.debugtools;

import javax.swing.*;
import java.awt.*;

public class AALFrame extends JFrame {

    public AALFrame(AdvancedActionLogger aal) {
        super("QuailJ Advanced Action Logger");
        setPreferredSize(new Dimension(600, 400));
        setSize(600, 400);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JTextArea text = new JTextArea(aal.logText);
        text.setFont(new Font("Serif", Font.ITALIC, 18));
        text.setLineWrap(true);
        text.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(text);
        getContentPane().add("Center", scrollPane);
        setVisible(true);
    }
}