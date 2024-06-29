package com.flyPlane;

import com.flyPlane.Tools.BackgroundPanel;
import com.flyPlane.Tools.JDBCUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class historicalPurchases extends JFrame {
    private static String loginUsername;
    private JPanel container;
    private JButton returnButton;
    private JLabel titleLabel;
    private JButton button1;
    private JTable historyTable;
    private JLabel reservationLabel;
    private static final String[] col = {"航班号", "飞机型号", "起点站", "终点站", "飞行日期", "舱位等级", "金额", "状态"};
    private static final int MAX_TICKETS = 100;
    Object[][] data = null;
    private static Object[] selectedData = null;

    public historicalPurchases(String username) {
        super("iwei 航空客运订票系统");
        loginUsername = username;

        setContentPane(new BackgroundPanel());
        add(container);
        setSize(630, 550);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Font font = new Font("宋体", Font.BOLD, 24);
        titleLabel.setFont(font);

        //  初始化 historyTable 数据
        try {
            Connection connection = JDBCUtil.getConnection();
            String sql = "SELECT * FROM ticketing WHERE username = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, loginUsername);
            ResultSet resultSet = preparedStatement.executeQuery();

            data = new Object[MAX_TICKETS][col.length];
            int nowRow = 0;
            while (resultSet.next()) {
                String flightNumber = resultSet.getString("flightNumber");
                String navigationLevel = resultSet.getString("navigationLevel");
                String state = resultSet.getString("state");
                Object[] rowArray = {flightNumber, null, null, null, null, navigationLevel, null, state};
                data[nowRow] = rowArray;

                String sql2 = "SELECT planeType, startStation, endStation, flyTime, Money FROM plane WHERE flightNumber = ?;";
                PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                preparedStatement2.setString(1, (String)data[nowRow][0]);
                ResultSet resultSet1 = preparedStatement2.executeQuery();
                if (resultSet1.next()) {
                    data[nowRow][1] = resultSet1.getString("planeType");
                    data[nowRow][2] = resultSet1.getString("startStation");
                    data[nowRow][3] = resultSet1.getString("endStation");
                    data[nowRow][4] = resultSet1.getDate("flyTime");
                    data[nowRow][6] = resultSet1.getString("Money");
                }
                resultSet1.close();
                preparedStatement2.close();
                nowRow ++ ;
            }

            //  已购票 的状态是订票量
            int nowReservation = 0;
            String sql2 = "SELECT * FROM ticketing WHERE username = ? AND state = '已购票';";
            preparedStatement = connection.prepareStatement(sql2);
            preparedStatement.setString(1, loginUsername);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                nowReservation ++ ;
            }

            resultSet.close();
            preparedStatement.close();
            JDBCUtil.release();

            historyTable.setModel(new DefaultTableModel(data, col));
            reservationLabel.setText("您目前的订票量为:" + nowReservation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        returnButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    iweiPlane iweiPlane = new iweiPlane(loginUsername);
                    iweiPlane.setVisible(true);
                    setVisible(false);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        historyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //  处理鼠标点击事件
                int row = historyTable.rowAtPoint(e.getPoint());
                Object[] rowData = data[row];
                if (rowData[0] == null) {
                    JOptionPane.showMessageDialog(null, "不可选择空列！请重新选择！");
                    return;
                }
                selectedData = rowData;
            }
        });
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //  根据 selectedData 处理退票业务
                if (selectedData == null) {
                    JOptionPane.showMessageDialog(null, "请选择一个航班后再点击退票按钮！");
                } else {
                    int op = JOptionPane.showConfirmDialog(null, "您选择退票的航班是: , 确定要退票吗？", "退票操作", JOptionPane.YES_NO_OPTION);
                    //  0 代表 '退票', 1 代表 '不退'
                    if (op == 0) {
                        if (selectedData[7].equals("已退票")) {
                            JOptionPane.showMessageDialog(null, "该次航班已退票，不可重复操作！");
                            return;
                        }
                        try {
                            /**
                             * 退票
                             * 1、将 plane 表中的对应航班的对应舱位的余票量 remainTickets + 1
                             * 2、将 user 表中的订票量 reservation - 1
                             * 3、将 ticketing 表中的对应记录的 state 改为 '已退票'
                             */
                            
                            //  1.
                            Connection connection = JDBCUtil.getConnection();
                            String sql1 = "UPDATE plane SET remainTickets = remainTickets + 1 WHERE flightNumber = ? AND planeType = ? AND startStation = ? AND endStation = ? AND flyTime = ? AND levelType = ?;";
                            PreparedStatement preparedStatement1 = connection.prepareStatement(sql1);
                            preparedStatement1.setString(1, (String)selectedData[0]);
                            preparedStatement1.setString(2, (String)selectedData[1]);
                            preparedStatement1.setString(3, (String)selectedData[2]);
                            preparedStatement1.setString(4, (String) selectedData[3]);
                            preparedStatement1.setDate(5, (Date)selectedData[4]);
                            preparedStatement1.setString(6, (String)selectedData[5]);
                            int op1 = preparedStatement1.executeUpdate();

                            //  2.
                            String sql2 = "UPDATE user SET reservation = reservation - 1 WHERE username = ?;";
                            PreparedStatement preparedStatement2 = connection.prepareStatement(sql2);
                            preparedStatement2.setString(1, loginUsername);
                            int op2 = preparedStatement2.executeUpdate();

                            //  3.
                            String sql3 = "UPDATE ticketing SET state = '已退票' WHERE username = ? AND flightNumber = ?;";
                            PreparedStatement preparedStatement3 = connection.prepareStatement(sql3);
                            preparedStatement3.setString(1, loginUsername);
                            preparedStatement3.setString(2, (String)selectedData[0]);
                            int op3 = preparedStatement3.executeUpdate();

                            //  判断退票是否成功
                            if (op1 > 0 && op2 > 0 && op3 > 0) {
                                JOptionPane.showMessageDialog(null, "退票成功！请重新进入该页面查看最新订票信息！");
                            }

                            preparedStatement1.close();
                            preparedStatement2.close();
                            preparedStatement3.close();
                            JDBCUtil.release();
                        } catch (Exception ee) {
                            throw new RuntimeException(ee);
                        }
                    }
                }
            }
        });
    }
}
