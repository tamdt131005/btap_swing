package com.mycompany.btap.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public final class DBConnection {
    private DBConnection() { }

    public static Connection getConnection() throws SQLException {
        Map<String, String> env = System.getenv();
        String host = env.getOrDefault("DB_HOST", "localhost");
        String port = env.getOrDefault("DB_PORT", "3306");
        String db = env.getOrDefault("DB_NAME", "btap_swing");
        String user = env.getOrDefault("DB_USER", "root");
        String pass = env.getOrDefault("DB_PASSWORD", "");

        host = System.getProperty("db.host", host);
        port = System.getProperty("db.port", port);
        db = System.getProperty("db.name", db);
        user = System.getProperty("db.user", user);
        pass = System.getProperty("db.password", pass);

        String url = String.format(
                "jdbc:mysql://%s:%s/%s?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                host, port, db);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }

        return DriverManager.getConnection(url, user, pass);
    }
}
