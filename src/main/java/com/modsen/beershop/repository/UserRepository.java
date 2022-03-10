package com.modsen.beershop.repository;

import com.modsen.beershop.service.exceprion.UnableToExecuteQuery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public static final String SELECT_USER_BY_LOGIN = "select * from users where login = ?";

    public boolean isExistUserByLogin(String name) {
        try (Connection connection = ConnectionPool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_USER_BY_LOGIN);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            return resultSet.next();
        } catch (SQLException e) {
            throw new UnableToExecuteQuery(e.getMessage());
        }
    }

}
