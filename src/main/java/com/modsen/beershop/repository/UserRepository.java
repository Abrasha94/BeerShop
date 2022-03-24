package com.modsen.beershop.repository;

import com.modsen.beershop.model.User;
import com.modsen.beershop.service.exception.UnableToExecuteQueryException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public enum UserRepository {
    INSTANCE;

    private static final String SELECT_USER_BY_LOGIN_OR_EMAIL = "select * from users where login = ? or email = ?";
    private static final String SELECT_USERS_BY_LOGIN_AND_PASSWORD = "select * from users where login = ? and pass = ?";
    private static final String CREATE_USER = "insert into users (login, pass, email, uuid, role)" +
            "values (?, ?, ?, ?, ?)";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "pass";
    private static final String EMAIL = "email";
    private static final String USER_UUID = "uuid";
    private static final String ROLE = "role";

    public boolean isExistUserByLoginOrEmail(String login, String email) {
        try (final PreparedStatement ps = ConnectionPool.getPreparedStatement(SELECT_USER_BY_LOGIN_OR_EMAIL)) {
            ps.setString(1, login);
            ps.setString(2, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public void createUser(String login, String pass, String email, UUID uuid, String role) {
        try (final PreparedStatement ps = ConnectionPool.getPreparedStatement(CREATE_USER)) {
            ps.setString(1, login);
            ps.setString(2, pass);
            ps.setString(3, email);
            ps.setObject(4, uuid);
            ps.setString(5, role);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public Optional<User> readUserByLoginAndPassword(String login, String password) {
        User user = null;
        try (final PreparedStatement ps = ConnectionPool.getPreparedStatement(SELECT_USERS_BY_LOGIN_AND_PASSWORD)) {
            ps.setString(1, login);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    user = User.builder()
                            .login(rs.getString(LOGIN))
                            .pass(rs.getString(PASSWORD))
                            .email(rs.getString(EMAIL))
                            .uuid(rs.getObject(USER_UUID, UUID.class))
                            .role(rs.getString(ROLE))
                            .build();
                }
                return Optional.ofNullable(user);
            }
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

}
