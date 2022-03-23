package com.modsen.beershop.repository;

import com.modsen.beershop.controller.dto.BuyBeerDto;
import com.modsen.beershop.model.UserTransaction;
import com.modsen.beershop.service.exceprion.UnableToExecuteQueryException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public enum UserTransactionRepository {
    INSTANCE;

    public static final String SELECT_USERS_TRANSACTIONS_BY_USER_ID = "select * from users_transactions where user_id = ? order by id limit ? offset ?";
    public static final String BEER_ID = "beer_id";
    public static final String TIME_OF_SALE = "time_of_sale";
    public static final String QUANTITY = "quantity";
    public static final String SELECT_ALL = "select * from users_transactions order by id limit ? offset ?";
    public static final String USER_ID = "user_id";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public static final String INSERT_INTO_USERS_TRANSACTIONS = "insert into users_transactions (user_id, beer_id, time_of_sale, quantity) values (?, ?, ?, to_json(?::json))";

    public void create(BuyBeerDto buyBeerDto) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_INTO_USERS_TRANSACTIONS)) {
            ps.setInt(1, buyBeerDto.getUserId());
            ps.setInt(2, buyBeerDto.getBeer().getId());
            ps.setTimestamp(3, Timestamp.from(Instant.now()));
            ps.setString(4, objectMapper.writeValueAsString(buyBeerDto.getQuantity()));
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<UserTransaction> readByUserId(Integer id, Integer pageSize, Integer page) {
        final List<UserTransaction> userTransactions = new ArrayList<>();
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_USERS_TRANSACTIONS_BY_USER_ID)) {
            ps.setInt(1, id);
            ps.setInt(2, pageSize);
            ps.setInt(3, pageSize * (page - 1));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    userTransactions.add(UserTransaction.builder()
                            .beerId(rs.getInt(BEER_ID))
                            .timeOfSale(rs.getTimestamp(TIME_OF_SALE).toInstant())
                            .quantity(objectMapper.readValue(rs.getString(QUANTITY), Object.class))
                            .build());
                }
                return userTransactions;
            } catch (IOException e) {
                throw new UnableToExecuteQueryException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public List<UserTransaction> readAll(Integer pageSize, Integer page) {
        final List<UserTransaction> usersTransactions = new ArrayList<>();
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ALL)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, pageSize * (page - 1));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    usersTransactions.add(UserTransaction.builder()
                            .userId(rs.getInt(USER_ID))
                            .beerId(rs.getInt(BEER_ID))
                            .build());
                }
                return usersTransactions;
            } catch (SQLException e) {
                throw new UnableToExecuteQueryException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }
}
