package dev.alex96jvm.javaintensive.dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        ConfigLoader configLoader = new ConfigLoader("db.properties");
        URL = configLoader.getProperty("db.url");
        USER = configLoader.getProperty("db.user");
        PASSWORD = configLoader.getProperty("db.password");
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}