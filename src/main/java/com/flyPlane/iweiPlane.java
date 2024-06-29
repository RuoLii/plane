package com.flyPlane;

import com.flyPlane.Tools.BackgroundPanel;
import com.flyPlane.Tools.JDBCUtil;
import jdk.nashorn.internal.scripts.JD;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.*;

public class iweiPlane extends JFrame {
    private static String loginUsername;
    private JPanel container;
    private JLabel titleLabel;
    private JButton logoutButton;
    private JButton bookButton;
    private JButton levelButton;
    private JTextField startField;
    private JTextField endField;
    private JButton button3;
    private JButton historyButton;
    private JTable planeTable;
    private JButton button1;
    private static final String[] col = {"航班号", "起点站", "终点站", "飞行日期", "飞机型号", "舱位等级", "余票量", "金额"};
    private static final int MAX_TICKETS = 100;
    private Object[][] data = null;
    private static Object[] selectedData = null;

    public iweiPlane(String username) throws SQLException {
        super("iwei 航空客运订票系统");
        loginUsername = username;
        setContentPane(new BackgroundPanel());
        add(container);
        setSize(630, 750);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Font font = new Font("宋体", Font.BOLD, 24);
        titleLabel.setFont(font);

        //  初始化 planeTable
        Connection connection = JDBCUtil.getConnection();
        String sql = "SELECT * FROM plane";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        data = new Object[MAX_TICKETS][col.length];
        int nowRow = 0;
        while (resultSet.next()) {
            String flightNumber = resultSet.getString("flightNumber");
            String startStation = resultSet.getString("startStation");
            String endStation = resultSet.getString("endStation");
            Date flyTime = resultSet.getDate("flyTime");
            String planeType = resultSet.getString("planeType");
            String levelType = resultSet.getString("levelType");
            int remainTickets = resultSet.getInt("remainTickets");
            int Money = resultSet.getInt("Money");

            Object[] rowArray = {flightNumber, startStation, endStation, flyTime, planeType, levelType, remainTickets, Money};
            data[nowRow ++ ] = rowArray;
        }
        resultSet.close();
        preparedStatement.close();
        JDBCUtil.release();

        planeTable.setModel(new DefaultTableModel(data, col));

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginFrame loginFrame = new loginFrame();
                loginFrame.setVisible(true);
                setVisible(false);
            }
        });
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String a = startField.getText();
                String b = endField.getText();
                startField.setText(b);
                endField.setText(a);
            }
        });
        levelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String a = levelButton.getText();
                if (a == "经济舱位")
                    levelButton.setText("头等舱位");
                else if (a == "头等舱位") {
                    levelButton.setText("所有舱位");
                } else {
                    levelButton.setText("经济舱位");
                }
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String start = startField.getText();
                String end = endField.getText();
                String level = levelButton.getText();

                //  全都为空，展示所有数据
                if (start.isEmpty() && end.isEmpty()) {
                    try {
                        Connection connection = JDBCUtil.getConnection();
                        PreparedStatement preparedStatement = null;
                        ResultSet resultSet = null;
                        String sql;
                        if (level.equals("所有舱位")) {
                            sql = "SELECT * FROM plane";
                            preparedStatement = connection.prepareStatement(sql);
                        } else {
                            sql = "SELECT * FROM plane WHERE levelType = ?";
                            preparedStatement = connection.prepareStatement(sql);
                            preparedStatement.setString(1, level);
                        }
                        resultSet = preparedStatement.executeQuery();

                        data = new Object[MAX_TICKETS][col.length];
                        int nowRow = 0;
                        while (resultSet.next()) {
                            String flightNumber = resultSet.getString("flightNumber");
                            String startStation = resultSet.getString("startStation");
                            String endStation = resultSet.getString("endStation");
                            Date flyTime = resultSet.getDate("flyTime");
                            String planeType = resultSet.getString("planeType");
                            String levelType = resultSet.getString("levelType");
                            int remainTickets = resultSet.getInt("remainTickets");
                            int Money = resultSet.getInt("Money");

                            Object[] rowArray = {flightNumber, startStation, endStation, flyTime, planeType, levelType, remainTickets, Money};
                            data[nowRow ++ ] = rowArray;
                        }

                        resultSet.close();
                        preparedStatement.close();
                        JDBCUtil.release();

                        planeTable.setModel(new DefaultTableModel(data, col));
                    } catch (Exception ee) {
                        throw new RuntimeException(ee);
                    }
                } else if (start.isEmpty() || end.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "起点站或终点站不能为空，请重新输入！");
                    startField.setText("");
                    endField.setText("");
                } else {
                    try {
                        Connection connection1;
                        String sql;
                        PreparedStatement preparedStatement1;
                        if ("所有舱位".equals(level)) {
                            connection1 = JDBCUtil.getConnection();
                            sql = "SELECT * FROM plane WHERE startStation = ? AND endStation = ?";
                            preparedStatement1 = connection1.prepareStatement(sql);
                            preparedStatement1.setString(1, start);
                            preparedStatement1.setString(2, end);
                        } else {
                            connection1 = JDBCUtil.getConnection();
                            sql = "SELECT * FROM plane WHERE startStation = ? AND endStation = ? AND levelType = ?";
                            preparedStatement1 = connection1.prepareStatement(sql);
                            preparedStatement1.setString(1, start);
                            preparedStatement1.setString(2, end);
                            preparedStatement1.setString(3, level);
                        }

                        ResultSet resultSet = preparedStatement1.executeQuery();

                        data = new Object[MAX_TICKETS][col.length];
                        int nowRow = 0;

                        while (resultSet.next()) {
                            String flightNumber = resultSet.getString("flightNumber");
                            String startStation = resultSet.getString("startStation");
                            String endStation = resultSet.getString("endStation");
                            Date flyTime = resultSet.getDate("flyTime");
                            String planeType = resultSet.getString("planeType");
                            String levelType = resultSet.getString("levelType");
                            int remainTickets = resultSet.getInt("remainTickets");
                            int Money = resultSet.getInt("Money");

                            Object[] rowArray = {flightNumber, startStation, endStation, flyTime, planeType, levelType, remainTickets, Money};
                            data[nowRow ++ ] = rowArray;
                        }
                        planeTable.setModel(new DefaultTableModel(data, col));

                        resultSet.close();
                        preparedStatement1.close();
                        JDBCUtil.release();
                    } catch (Exception ee) {
                        throw new RuntimeException(ee);
                    }
                }
            }
        });
        planeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //  处理鼠标点击事件
                int row = planeTable.rowAtPoint(e.getPoint());
                Object[] rowData = data[row];
                if (rowData[0] == null) {
                    JOptionPane.showMessageDialog(null, "不可选择空列！请重新选择！");
                    return;
                }
                selectedData = rowData;
//                for (int i = 0; i < col.length; i ++ )
//                    System.out.print(rowData[i] + " ");
            }
        });
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //  根据 selectedData 处理订票业务
                if (selectedData == null) {
                    JOptionPane.showMessageDialog(null, "请选择一个航班后再点击预订按钮！");
                } else {
                    int num = (int) selectedData[6]; //  余票量
                    if (num <= 0) {
                        JOptionPane.showMessageDialog(null, "很抱歉，该航班该舱位的票量已售罄，无法进行预订！");
                        selectedData = null;
                        return;
                    }
                    int op = JOptionPane.showConfirmDialog(null, "您选择的航班是: " + selectedData[0] + ", 点击提交订单！", "确认订单", JOptionPane.YES_NO_OPTION);
                    //  0 代表 '是', 1 代表 '否'
                    if (op == 0) {
                        try {
                            /**
                             * 订票
                             * 1、查询 ticketing 表中是否已有该航班的数据记录
                             * 1、将 plane 表中的对应航班的对应舱位的余票量 remainTickets - 1
                             * 2、将 user 表中的订票量 reservation + 1
                             * 3、向 ticketing 表中插入记录
                             */

                            Connection connection = JDBCUtil.getConnection();
                            //  1.
                            String sql1 = "SELECT * FROM ticketing WHERE username = ? AND flightNumber = ?";
                            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                            preparedStatement1.setString(1, loginUsername);
                            preparedStatement1.setString(2, (String)selectedData[0]);
                            ResultSet resultSet = preparedStatement1.executeQuery();
                            while (resultSet.next()) {
                                JOptionPane.showMessageDialog(null, "您已购入该次航班的机票，不可重复操作！");
                                resultSet.close();
                                preparedStatement1.close();
                                JDBCUtil.release();
                                return;
                            }

                            //  2.
                            String sql2 = "UPDATE plane SET remainTickets = remainTickets - 1 WHERE flightNumber = ? AND startStation = ? AND endStation = ? AND flyTime = ? AND planeType = ? AND levelType = ?;";
                            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                            preparedStatement2.setString(1, (String)selectedData[0]);
                            preparedStatement2.setString(2, (String)selectedData[1]);
                            preparedStatement2.setString(3, (String)selectedData[2]);
                            preparedStatement2.setDate(4, (Date)selectedData[3]);
                            preparedStatement2.setString(5, (String)selectedData[4]);
                            preparedStatement2.setString(6, (String)selectedData[5]);
                            int op2 = preparedStatement2.executeUpdate();

                            //  3.
                            String sql3 = "UPDATE user SET reservation = reservation + 1 WHERE username = ?;";
                            PreparedStatement preparedStatement3 = connection.prepareStatement(sql3);
                            preparedStatement3.setString(1, loginUsername);
                            int op3 = preparedStatement3.executeUpdate();

                            //  4.
                            String sql4 = "INSERT INTO ticketing (username, flightNumber, navigationLevel, state) VALUES (?, ?, ?, '已购票')";
                            PreparedStatement preparedStatement4 = connection.prepareStatement(sql4);
                            preparedStatement4.setString(1, loginUsername);
                            preparedStatement4.setString(2, (String)selectedData[0]);
                            preparedStatement4.setString(3, (String)selectedData[5]);
                            int op4 = preparedStatement4.executeUpdate();

                            //  判断订票是否成功
                            if (op2 > 0 && op3 > 0 && op4 > 0) {
                                JOptionPane.showMessageDialog(null, "订票成功！可前往 '查询历史机票' 界面查看订票信息！");
                            }

                            resultSet.close();
                            preparedStatement1.close();
                            preparedStatement2.close();
                            preparedStatement3.close();
                            preparedStatement4.close();
                            JDBCUtil.release();
                        } catch (SQLException ee) {
                            throw new RuntimeException(ee);
                        }
                    }
                }
            }
        });
        historyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                historicalPurchases historicalPurchases = new historicalPurchases(loginUsername);
                historicalPurchases.setVisible(true);
                setVisible(false);
            }
        });
    }
}
