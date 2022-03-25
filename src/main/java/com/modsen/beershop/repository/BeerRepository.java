package com.modsen.beershop.repository;

import com.modsen.beershop.controller.dto.CreateBeerDto;
import com.modsen.beershop.model.Beer;
import com.modsen.beershop.service.exception.UnableToExecuteQueryException;

import java.io.IOException;
import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.modsen.beershop.utils.ObjectMapperBean.objectMapper;

public enum BeerRepository {
    INSTANCE;

    public static final String SELECT_BEER_BY_NAME_AND_CONTAINER =
            "select * from beers where name = ? and container = ?";
    public static final String SELECT_BEER_BY_ID = "select * from beers where id = ?";
    public static final String SELECT_ACTIVE_BEERS = "select * from beers where id " +
            "not in (select id from beers where beer_description::jsonb @> '{\"quantity\" : 0}' " +
            "or beer_description::jsonb @> '{\"quantity\" : 0.0}') order by id limit ? offset ?";
    public static final String CREATE_BEER =
            "insert into beers (name, container, type, abv, ibu, created, update, beer_description, quantity) " +
                    "values (?, ?, ?, ?, ?, ?, ?, to_json(?::json), ?)";
    public static final String UPDATE_BEER_BY_ID = "update beers set quantity = ?, update = ? " +
            "where id = ?";
    public static final String UPDATE_BEER_QUANTITY = "update beers set quantity = ? " +
            "where name = ? and container = ?";
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CONTAINER = "container";
    public static final String TYPE = "type";
    public static final String ABV = "abv";
    public static final String IBU = "ibu";
    public static final String BEER_DESCRIPTION = "beer_description";
    public static final String QUANTITY = "quantity";


    public boolean isExistBeerById(Integer id) {
        try (final PreparedStatement ps = ConnectionPool.getPreparedStatement(SELECT_BEER_BY_ID)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public boolean isExistBeerByNameAndContainer(String name, String container) {
        try (final PreparedStatement ps = ConnectionPool.getPreparedStatement(SELECT_BEER_BY_NAME_AND_CONTAINER)) {
            ps.setString(1, name);
            ps.setString(2, container);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public CreateBeerDto createBeer(CreateBeerDto createBeerDto) {
        try (final PreparedStatement ps = ConnectionPool.getPreparedStatement(CREATE_BEER)) {
            ps.setString(1, createBeerDto.getName());
            ps.setString(2, createBeerDto.getContainer());
            ps.setString(3, createBeerDto.getType());
            ps.setDouble(4, createBeerDto.getAbv());
            ps.setInt(5, createBeerDto.getIbu());
            ps.setTimestamp(6, Timestamp.from(Instant.now()));
            ps.setTimestamp(7, Timestamp.from(Instant.now()));
            ps.setString(8, objectMapper.writeValueAsString(createBeerDto.getBeerDescription()));
            ps.setInt(9, createBeerDto.getQuantity());
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    createBeerDto.setId(rs.getInt(1));
                }
                return createBeerDto;
            }
        } catch (SQLException | IOException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public void updateBeerTable(Integer quantity, Integer id) {
        try (final PreparedStatement ps = ConnectionPool.getPreparedStatement(UPDATE_BEER_BY_ID)) {
            ps.setInt(1, quantity);
            ps.setTimestamp(2, Timestamp.from(Instant.now()));
            ps.setInt(3, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public <T> Optional<Beer> readBeerById(Integer id) {
        Beer beer = null;
        try (final PreparedStatement ps = ConnectionPool.getPreparedStatement(SELECT_BEER_BY_ID)) {
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
                            .beerDescription(rs.getString(BEER_DESCRIPTION))
                            .quantity(rs.getInt(QUANTITY))
                            .build();
                }
            }
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
        return Optional.ofNullable(beer);
    }

    public void updateBeerQuantity(Beer beer) {
        try (final PreparedStatement ps = ConnectionPool.getPreparedStatement(UPDATE_BEER_QUANTITY)) {
            ps.setInt(1, beer.getQuantity());
            ps.setString(2, beer.getName());
            ps.setString(3, beer.getContainer());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new UnableToExecuteQueryException(e.getMessage());
        }
    }

    public List<Beer> readActiveBeers(Integer pageSize, Integer page) {
        List<Beer> beers = new ArrayList<>();
        try (final PreparedStatement ps = ConnectionPool.getPreparedStatement(SELECT_ACTIVE_BEERS)) {
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
