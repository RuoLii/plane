package com.flyPlane;

import com.flyPlane.Tools.BackgroundPanel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class iweiPlane extends JFrame {
    private JPanel panel;
    private JButton registerButton;
    private JButton loginButton;
    private JTextField textField1;
    private JTextField textField2;

    public iweiPlane() {
        super("iwei 航空客运订票系统");
        setContentPane(new BackgroundPanel());
        add(panel);
        setSize(400, 375);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //  注册按钮点击事件
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerFrame registerFrame = new registerFrame();
                registerFrame.setVisible(true);
                setVisible(false);
            }
        });
        //  登录按钮点击事件

    }
}
