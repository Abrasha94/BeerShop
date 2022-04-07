package com.modsen.beershop.repository;

import com.modsen.beershop.controller.dto.CreateBeerDto;
import com.modsen.beershop.model.Beer;
import com.modsen.beershop.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

public enum BeerRepository {
    INSTANCE;

    public static final String SELECT_BEER_BY_NAME_AND_CONTAINER =
            "select * from beers where name = ? and container = ?";
    public static final String SELECT_ACTIVE_BEERS = "select * from beers " +
            "where quantity > 0 order by id limit ? offset ?";

    public boolean isExistBeerById(Integer id) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            final Beer beer = session.get(Beer.class, id);
            return !(beer == null);
        }
    }

    public boolean isExistBeerByNameAndContainer(String name, String container) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            final Beer beer = session.createNativeQuery(SELECT_BEER_BY_NAME_AND_CONTAINER, Beer.class)
                    .setParameter(1, name)
                    .setParameter(2, container)
                    .getSingleResult();
            return !(beer == null);
        }
    }

    public CreateBeerDto createBeer(CreateBeerDto createBeerDto) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            final Transaction transaction = session.beginTransaction();
            final Integer id = (Integer) session.save(Beer.builder()
                    .name(createBeerDto.getName())
                    .container(createBeerDto.getContainer())
                    .type(createBeerDto.getType())
                    .abv(createBeerDto.getAbv())
                    .ibu(createBeerDto.getIbu())
                    .beerDescription(createBeerDto.getBeerDescription())
                    .created(Timestamp.from(Instant.now()))
                    .update(Timestamp.from(Instant.now()))
                    .quantity(createBeerDto.getQuantity())
                    .build());
            createBeerDto.setId(id);
            transaction.commit();
            return createBeerDto;
        }
    }

    public void updateBeerTable(Integer quantity, Integer id) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            final Beer beer = session.get(Beer.class, id);
            beer.setQuantity(quantity);
            beer.setUpdate(Timestamp.from(Instant.now()));
            final Transaction transaction = session.beginTransaction();
            session.update(beer);
            transaction.commit();
        }
    }

    public <T> Optional<Beer> readBeerById(Integer id) {

        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            final Beer beer = session.get(Beer.class, id);
            return Optional.ofNullable(beer);
        }
    }

    public void updateBeerQuantity(Beer beer) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            final Beer beer1 = session.get(Beer.class, beer.getId());
            final Transaction transaction = session.beginTransaction();
            session.update(beer1);
            transaction.commit();
        }
    }

    public List<Beer> readActiveBeers(Integer pageSize, Integer page) {
        try (final Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNativeQuery(SELECT_ACTIVE_BEERS, Beer.class)
                    .setParameter(1, pageSize)
                    .setParameter(2, pageSize * (page - 1))
                    .list();

        }
    }

}
