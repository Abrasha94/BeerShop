package com.modsen.beershop.repository;

import com.modsen.beershop.repository.config.Configuration;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

    private static final HikariDataSource dataSource;

    static {
        dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(Configuration.INSTANCE.getURL());
        dataSource.setUsername(Configuration.INSTANCE.getUser());
        dataSource.setPassword(Configuration.INSTANCE.getPassword());
    }

    private ConnectionPool() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
