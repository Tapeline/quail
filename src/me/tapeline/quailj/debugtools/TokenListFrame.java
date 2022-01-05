package me.tapeline.quailj.debugtools;

import me.tapeline.quailj.tokenizetools.tokens.Token;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Vector;

public class TokenListFrame extends JFrame {

    public TokenListFrame(List<Token> tokens) {
        super("QuailJ Token List Viewer");
        setPreferredSize(new Dimension(600, 400));
        setSize(600, 400);
        setResizable(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Vector<String> stringList = new Vector<>();
        for (int i = 0; i < tokens.size(); i++) stringList.add(i + " | " + tokens.get(i).srepr());
        JList<String> list = new JList<>(stringList);
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(list);
        getContentPane().add("Center", scrollPane);
        setVisible(true);
    }
}