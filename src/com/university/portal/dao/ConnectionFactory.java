package com.university.portal.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static String jdbcUrl = "jdbc:h2:mem:university;DB_CLOSE_DELAY=-1";
    private static String jdbcUser = "sa";
    private static String jdbcPassword = "";

    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException ignored) {
        }
    }

    public static void configure(String url, String user, String password) {
        jdbcUrl = url;
        jdbcUser = user;
        jdbcPassword = password;
        if (jdbcUrl != null && jdbcUrl.startsWith("jdbc:mysql:")) {
            try { Class.forName("com.mysql.cj.jdbc.Driver"); } catch (ClassNotFoundException ignored) {}
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(jdbcUrl, jdbcUser, jdbcPassword);
    }
}
