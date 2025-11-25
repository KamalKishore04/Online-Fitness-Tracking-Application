package com.fitnesstracker.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    // TODO: change DB name/user/password according to your local MySQL
    private static final String URL = "jdbc:mysql://localhost:3306/fitness_db";
    private static final String USER = "root";                 // your MySQL user
    private static final String PASSWORD = "Kamal123"; // you already set this

    static {
        try {
            // Load MySQL driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC driver not found in classpath", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}

