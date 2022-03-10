package com.modsen.beershop.repository;

import com.modsen.beershop.model.User;
import com.modsen.beershop.service.exceprion.UnableToExecuteQueryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class UserRepository {

    public static final String SELECT_USER_BY_LOGIN_OR_EMAIL = "select * from users where login = ? or email = ?";
    private static final String CREATE_USER = "insert into users (login, pass, email, uuid, role)" +
            "values (?, ?, ?, ?, ?)";
    private static final String SELECT_USERS_BY_LOGIN_AND_PASSWORD = "select * from users where login = ? and pass = ?";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "pass";
    private static final String EMAIL = "email";
    private static final String UUID = "uuid";
    private static final String ROLE = "role";

    public boolean isExistUserByLoginOrEmail(String name, String email) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_USER_BY_LOGIN_OR_EMAIL)) {
            ps.setString(1, name);
            ps.setString(2, email);
            try (ResultSet resultSet = ps.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public void createUser(String login, String pass, String email, String uuid, String role) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(CREATE_USER)) {
            ps.setString(1, login);
            ps.setString(2, pass);
            ps.setString(3, email);
            ps.setString(3, uuid);
            ps.setString(3, role);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public Optional<User> readUserByLoginAndPassword(String login, String password) {
        User user = null;
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_USERS_BY_LOGIN_AND_PASSWORD)) {
            ps.setString(1, login);
            ps.setString(2, password);
            try (ResultSet resultSet = ps.executeQuery()) {
                while (resultSet.next()) {
                    user = User.builder()
                            .login(resultSet.getString(LOGIN))
                            .pass(resultSet.getString(PASSWORD))
                            .email(resultSet.getString(EMAIL))
                            .uuid(resultSet.getString(UUID))
                            .role(resultSet.getString(ROLE))
                            .build();
                }
                return Optional.ofNullable(user);
            }
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

}
