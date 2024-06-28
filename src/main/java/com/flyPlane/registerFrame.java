package com.flyPlane;

import com.flyPlane.Tools.BackgroundPanel;
import com.flyPlane.Tools.sql;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class registerFrame extends JFrame{
    private JPanel panel;
    private JTextField name;
    private JTextField username;
    private JTextField pwd;
    private JTextField repwd;
    private JButton registerButton;
    private JButton exit;

    public registerFrame() {
        super("iwei 航空客运订票系统");
        setContentPane(new BackgroundPanel());
        add(panel);
        setSize(450, 600);
        setResizable(false); // 设置窗口不可更改大小
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginFrame plane = new loginFrame();
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
                String nameStr = name.getText();
                if (usernameStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "用户名不能为空！");
                }else if (pwdStr.isEmpty() || repwdStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "密码不能为空！");
                }else if (!pwdStr.equals(repwdStr)) {
                    JOptionPane.showMessageDialog(null, "两次输入的密码不一致！");
                } else if (nameStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "姓名不能为空！");
                } else {
                    //  注册功能实现
                    try {
                        if (sql.register(nameStr, usernameStr, pwdStr)) {
                            JOptionPane.showMessageDialog(null, "注册成功！");
                            loginFrame loginFrame = new loginFrame();
                            loginFrame.setVisible(true);
                            setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(null, "用户名已存在，请重新输入！");
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
        });
    }
}
