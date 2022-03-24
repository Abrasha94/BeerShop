create table users_transactions
(
    id           serial not null
        constraint users_transactions_pk
            primary key,
    user_uuid      uuid
        constraint users_transactions_users_uuid_fk
            references users,
    beer_id      int
        constraint users_transactions_beers_id_fk
            references beers,
    time_of_sale timestamp,
    quantity     json
);

alter table users_transactions owner to postgres;