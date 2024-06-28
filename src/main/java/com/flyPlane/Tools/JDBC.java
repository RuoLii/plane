package com.flyPlane.Tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JDBC {
    public JDBC() throws Exception {
        Connection connection = DriverManager.getConnection("jdbc:mysql:///plane");

        String sql = "";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);


    }
}
