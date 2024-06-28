package com.flyPlane.Tools;

import javax.swing.*;
import java.awt.*;

// BackgroundPanel 类，用于绘制背景图片
public class BackgroundPanel extends JPanel {
    private final Image backgroundImage;

    public BackgroundPanel() {
        // 加载背景图片
        backgroundImage = new ImageIcon(getClass().getResource("/static/2.jpg")).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // 绘制背景图片
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}
