package org.scoula.database;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class JDBCUtil {
    static Connection conn = null;

    static {
        try {
            Properties properties = new Properties();

            InputStream inputStream = JDBCUtil.class
                .getClassLoader()
                .getResourceAsStream("application.properties");

            properties.load(inputStream);

            String driver = properties.getProperty("driver");
            String url = properties.getProperty("url");
            String id = properties.getProperty("id");
            String password = properties.getProperty("password");

            Class.forName(driver);
            conn = DriverManager.getConnection(url, id, password);

            System.out.println("DB 연결 성공");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return conn;
    }

    public static void close() {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("DB 연결 종료");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}