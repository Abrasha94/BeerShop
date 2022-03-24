package com.modsen.beershop.repository;

import com.modsen.beershop.config.Configuration;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public static PreparedStatement getPreparedStatement(String sql) throws SQLException {
        return dataSource.getConnection().prepareStatement(sql);
    }
}
