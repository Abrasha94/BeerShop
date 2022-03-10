create table beers
(
    id        serial           not null
        constraint beers_pk
            primary key,
    name      varchar(55),
    container varchar(10),
    type      varchar(10)      not null,
    abv       double precision not null,
    ibu       int              not null,
    created   timestamp,
    update    timestamp
);

alter table beers
    owner to postgres;