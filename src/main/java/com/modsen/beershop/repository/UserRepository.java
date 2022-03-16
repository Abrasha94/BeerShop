package com.modsen.beershop.repository;

import com.modsen.beershop.model.User;
import com.modsen.beershop.service.exceprion.UnableToExecuteQueryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public enum UserRepository {
    INSTANCE;

    private static final String SELECT_USER_BY_LOGIN_OR_EMAIL = "select * from users where login = ? or email = ?";
    private static final String CREATE_USER = "insert into users (login, pass, email, uuid, role)" +
            "values (?, ?, ?, ?, ?)";
    private static final String SELECT_USERS_BY_LOGIN_AND_PASSWORD = "select * from users where login = ? and pass = ?";
    private static final String LOGIN = "login";
    private static final String PASSWORD = "pass";
    private static final String EMAIL = "email";
    private static final String UUID = "uuid";
    private static final String ROLE = "role";
    public static final String ID = "id";

    public boolean isExistUserByLoginOrEmail(String login, String email) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_USER_BY_LOGIN_OR_EMAIL)) {
            ps.setString(1, login);
            ps.setString(2, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
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
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    user = User.builder()
                            .login(rs.getString(LOGIN))
                            .pass(rs.getString(PASSWORD))
                            .email(rs.getString(EMAIL))
                            .uuid(rs.getString(UUID))
                            .role(rs.getString(ROLE))
                            .build();
                }
                return Optional.ofNullable(user);
            }
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public Integer readUserIdByUuid(Object uuid) {
        Integer id = null;
        try (Connection conn= ConnectionPool.getConnection();
        PreparedStatement ps= conn.prepareStatement("select * from users where uuid = ?")){
            ps.setObject(1, uuid);
            try (ResultSet rs= ps.executeQuery()){
                while (rs.next()) {
                    id = rs.getInt(ID);
                }
                return id;
            }
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

}
