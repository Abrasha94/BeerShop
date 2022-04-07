package com.modsen.beershop.repository;

import com.modsen.beershop.controller.dto.BuyBeerDto;
import com.modsen.beershop.model.Beer;
import com.modsen.beershop.model.User;
import com.modsen.beershop.model.UserTransaction;
import com.modsen.beershop.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;


public enum UserTransactionRepository {
    INSTANCE;

    public static final String SELECT_USERS_TRANSACTIONS_BY_UUID = "select * from users_transactions " +
            "where user_uuid = ? order by id limit ? offset ?";
    public static final String SELECT_ALL = "select * from users_transactions order by id limit ? offset ?";

    public void create(BuyBeerDto buyBeerDto) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            final User user = session.get(User.class, buyBeerDto.getUserUuid());
            final Integer beerId = buyBeerDto.getBeer().getId();
            final Beer beer = session.get(Beer.class, beerId);
            final Transaction transaction = session.beginTransaction();
            session.save(UserTransaction.builder().user(user).beer(beer).timeOfSale(Timestamp.from(Instant.now())).quantity(buyBeerDto.getQuantity()));
            transaction.commit();
        }
    }

    public List<UserTransaction> readByUserUuid(UUID uuid, Integer pageSize, Integer page) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNativeQuery(SELECT_USERS_TRANSACTIONS_BY_UUID, UserTransaction.class)
                    .setParameter(1, uuid)
                    .setParameter(2, pageSize)
                    .setParameter(3, pageSize * (page - 1))
                    .list();
        }
    }

    public List<UserTransaction> readAll(Integer pageSize, Integer page) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNativeQuery(SELECT_ALL, UserTransaction.class)
                    .setParameter(1, pageSize)
                    .setParameter(2, pageSize * (page - 1))
                    .list();
        }
    }
}
