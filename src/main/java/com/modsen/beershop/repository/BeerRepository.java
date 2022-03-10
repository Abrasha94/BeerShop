package com.modsen.beershop.repository;

import com.modsen.beershop.service.exceprion.UnableToExecuteQueryException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BeerRepository {

    private static final String SELECT_BEER_BY_ID = "select * from beers where id = ?";

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

}
