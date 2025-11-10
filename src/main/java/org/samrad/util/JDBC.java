package org.samrad.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC {
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/xepdb1";
    private static final String USER = "hesam";
    private static final String PASSWORD = "myjava123";
    private static final String DRIVER = "oracle.jdbc.driver.OracleDriver";

    static {
        try{
            Class.forName(DRIVER);
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public static Connection getConnection()throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
