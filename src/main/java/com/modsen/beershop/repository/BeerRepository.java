package com.modsen.beershop.repository;

import com.modsen.beershop.controller.dto.AddBeerDto;
import com.modsen.beershop.controller.request.AddBeerRequest;
import com.modsen.beershop.service.exceprion.UnableToExecuteQueryException;
import lombok.RequiredArgsConstructor;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.time.Instant;

public enum BeerRepository {
    INSTANCE;

    private static final String SELECT_BEER_BY_ID = "select * from beers where id = ?";
    public static final String SELECT_BEER_BY_NAME_AND_CONTAINER =
            "select * from beers where name = ? and container = ?";
    public static final String CREATE_BEER =
            "insert into beers (name, container, type, abv, ibu, created, update, beer_description) " +
                    "values (?, ?, ?, ?, ?, ?, ?, to_json(?::json))";

    private final ObjectMapper objectMapper=new ObjectMapper();

    public boolean isExistBeerById(Integer id) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BEER_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public boolean isExistBeerByNameAndContainer(String name, String container) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BEER_BY_NAME_AND_CONTAINER)) {
            ps.setString(1, name);
            ps.setString(2, container);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public AddBeerDto createBeer(AddBeerDto addBeerDto) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(CREATE_BEER)) {
            ps.setString(1, addBeerDto.getName());
            ps.setString(2, addBeerDto.getContainer());
            ps.setString(3, addBeerDto.getType());
            ps.setDouble(4, addBeerDto.getAbv());
            ps.setInt(5, addBeerDto.getIbu());
            ps.setTimestamp(6, Timestamp.from(Instant.now()));
            ps.setTimestamp(7, Timestamp.from(Instant.now()));
            ps.setString(8, objectMapper.writeValueAsString(addBeerDto.getBeerDescription()));
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    addBeerDto.setId(rs.getInt(1));
                }
                return addBeerDto;
            }
        } catch (SQLException | IOException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

}
