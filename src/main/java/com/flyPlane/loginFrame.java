package com.flyPlane;

import com.flyPlane.Tools.BackgroundPanel;
import com.flyPlane.Tools.sql;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class loginFrame extends JFrame {
    private JPanel panel;
    private JButton registerButton;
    private JButton loginButton;
    private JTextField username;
    private JTextField pwd;

    public loginFrame() {
        super("iwei 航空客运订票系统");
        setContentPane(new BackgroundPanel());
        add(panel);
        setSize(450, 600);
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

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String usernameStr = username.getText();
                String pwdStr = pwd.getText();
                if (usernameStr.isEmpty() || pwdStr.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "账号或密码不能为空！");
                } else {
                    try {
                        if (sql.login(usernameStr, pwdStr)){
                            JOptionPane.showMessageDialog(null, "登录成功！");
                            //  跳转到订票系统
                            iweiPlane iweiPlane = new iweiPlane(usernameStr);
                            iweiPlane.setVisible(true);
                            setVisible(false);
                        } else {
                            JOptionPane.showMessageDialog(null, "用户名或密码错误！");
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
    }
}
