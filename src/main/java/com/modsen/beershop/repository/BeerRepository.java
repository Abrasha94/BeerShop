package com.modsen.beershop.repository;

import com.modsen.beershop.controller.dto.AddBeerDto;
import com.modsen.beershop.model.Beer;
import com.modsen.beershop.model.BeerDescription;
import com.modsen.beershop.service.exceprion.UnableToExecuteQueryException;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public enum BeerRepository {
    INSTANCE;

    public static final String SELECT_BEER_BY_NAME_AND_CONTAINER =
            "select * from beers where name = ? and container = ?";
    public static final String CREATE_BEER =
            "insert into beers (name, container, type, abv, ibu, created, update, beer_description) " +
                    "values (?, ?, ?, ?, ?, ?, ?, to_json(?::json))";
    public static final String UPDATE_BEER = "update beers set beer_description = to_json(?::json), update = ? " +
            "where id = ?";
    public static final String SELECT_BEER_BY_ID = "select * from beers where id = ?";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CONTAINER = "container";
    public static final String TYPE = "type";
    public static final String ABV = "abv";
    public static final String IBU = "ibu";
    public static final String BEER_DESCRIPTION = "beer_description";
    public static final String UPDATE_BEERS = "update beers set beer_description = to_json(?::json) " +
            "where name = ? and container = ?";
    public static final String SELECT_ACTIVE_BEERS = "select * from beers where id " +
            "not in (select id from beers where beer_description::jsonb @> '{\"quantity\" : 0}' " +
            "or beer_description::jsonb @> '{\"quantity\" : 0.0}') order by id limit ? offset ?";

    private final ObjectMapper objectMapper = new ObjectMapper();

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

    public void updateBeerDescription(BeerDescription beerDescription, Integer id) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_BEER)) {
            ps.setString(1, objectMapper.writeValueAsString(beerDescription));
            ps.setTimestamp(2, Timestamp.from(Instant.now()));
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public <T> Optional<Beer> readBeerById(Integer id, Class<T> tClass) {
        Beer beer = null;
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_BEER_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    beer = Beer.builder()
                            .id(rs.getInt(ID))
                            .name(rs.getString(NAME))
                            .container(rs.getString(CONTAINER))
                            .type(rs.getString(TYPE))
                            .abv(rs.getDouble(ABV))
                            .ibu(rs.getInt(IBU))
                            .beerDescription((BeerDescription) objectMapper.readValue(rs.getString(BEER_DESCRIPTION), tClass))
                            .build();
                }
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
        return Optional.ofNullable(beer);
    }

    public void updateBeer(Beer beer) {
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(UPDATE_BEERS)) {
            ps.setString(1, objectMapper.writeValueAsString(beer.getBeerDescription()));
            ps.setString(2, beer.getName());
            ps.setString(3, beer.getContainer());
            ps.executeUpdate();
        } catch (SQLException | IOException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public List<Beer> readActiveBeers(Integer pageSize, Integer page) {
        List<Beer> beers = new ArrayList<>();
        try (Connection conn = ConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(SELECT_ACTIVE_BEERS)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, pageSize * (page - 1));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    beers.add(Beer.builder()
                            .id(rs.getInt(ID))
                            .build());
                }
            }
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
        return beers;
    }

}
