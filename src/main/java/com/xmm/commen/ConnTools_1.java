package com.xmm.commen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by 肖明明 on 2017/3/22.
 */
public class ConnTools_1 implements ConnTools{

    private static String dirverClassName = "com.mysql.jdbc.Driver";
    private static String url = "jdbc:mysql://192.168.0.89:3306/yc_dataTranc?useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&tinyInt1isBit=false";
    private static String user = "root";
    private static String password = "manager1";
    
    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(dirverClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
}
