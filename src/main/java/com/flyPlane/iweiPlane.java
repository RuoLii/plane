package com.flyPlane;

import com.flyPlane.Tools.BackgroundPanel;

import javax.swing.*;

public class iweiPlane extends JFrame {

    private JPanel container;

    public iweiPlane() {
        super("iwei 航空客运订票系统");
        setContentPane(new BackgroundPanel());
        add(container);
        setSize(630, 750);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
