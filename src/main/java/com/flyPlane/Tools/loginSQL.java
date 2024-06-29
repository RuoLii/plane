package com.flyPlane.Tools;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * sql 类
 * 实现航空订票系统数据 CRUD 的 sql 语句实现
 */
public class loginSQL {
    //  注册
    public static boolean register(String name, String username, String password) throws SQLException {
        Connection connection = JDBCUtil.getConnection();
        String sql = "SELECT id FROM user WHERE username = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            //  用户已存在，注册失败，返回 false
            resultSet.close();
            preparedStatement.close();
            JDBCUtil.release();

            return false;
        } else {
            //  用户名符合要求，注册成功，执行插入语句并且返回 true
            resultSet.close();
            sql = "INSERT INTO user (name, username, password) VALUES (?, ?, ?);";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, username);
            preparedStatement.setString(3, password);
            int i = preparedStatement.executeUpdate();

            preparedStatement.close();
            JDBCUtil.release();

            if (i == 1) {
                return true;
            }
            return false;
        }
    }

    //  登录
    public static boolean login(String username, String password) throws SQLException {
        Connection connection = JDBCUtil.getConnection();
        String sql = "SELECT id FROM user WHERE username = ? AND `password` = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);

        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            resultSet.close();
            preparedStatement.close();
            JDBCUtil.release();
            return true;
        }
        return false;
    }
}
