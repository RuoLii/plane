package com.flyPlane;

import com.flyPlane.Tools.BackgroundPanel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class registerFrame extends JFrame{
    private JPanel panel;
    private JTextField username;
    private JTextField pwd;
    private JTextField repwd;
    private JButton registerButton;
    private JButton exit;

    public registerFrame() {
        super("iwei 航空客运订票系统");
        setContentPane(new BackgroundPanel());
        add(panel);
        setSize(400, 275);
        setResizable(false); // 设置窗口不可更改大小
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                iweiPlane plane = new iweiPlane();
                plane.setVisible(true);
                setVisible(false);
            }
        });


        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String usernameStr = username.getText();
                String pwdStr = pwd.getText();
                String repwdStr = repwd.getText();
                if (usernameStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "用户名不能为空！");
                }else if (pwdStr.isEmpty() || repwdStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "密码不能为空！");
                }else if (!pwdStr.equals(repwdStr)) {
                    JOptionPane.showMessageDialog(null, "两次输入的密码不一致！");
                } else if (usernameStr.equals("111")) {
                    JOptionPane.showMessageDialog(null, "用户名已存在，请重新输入！");
                }

                //  注册功能实现

                JOptionPane.showMessageDialog(null, "注册成功！");
                iweiPlane iweiPlane = new iweiPlane();
                iweiPlane.setVisible(true);
                setVisible(false);
            }
        });
    }
}
