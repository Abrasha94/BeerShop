package com.modsen.beershop.repository;

import com.modsen.beershop.repository.config.Configuration;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static final HikariDataSource dataSource;
    private static final Configuration configuration = new Configuration();

    static {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(configuration.getURL());
        dataSource.setUsername(configuration.getUser());
        dataSource.setPassword(configuration.getPassword());
    }

    private ConnectionPool() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
