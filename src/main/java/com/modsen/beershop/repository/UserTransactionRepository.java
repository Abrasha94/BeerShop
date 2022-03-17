package com.modsen.beershop.repository;

import com.modsen.beershop.controller.dto.BuyBeerDto;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;

public enum UserTransactionRepository {
    INSTANCE;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public static final String INSERT_INTO_USERS_TRANSACTIONS = "insert into users_transactions (user_id, beer_id, time_of_sale, quantity) values (?, ?, ?, to_json(?::json))";

    public void save(BuyBeerDto buyBeerDto) {
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
}
