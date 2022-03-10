create table users
(
    id        serial       not null
        constraint users_pk
            primary key,
    role      varchar(5),
    login     varchar(55)  not null,
    pass varchar(55)  not null,
    email     varchar(255) not null,
    uuid      varchar(36)
);

alter table users
    owner to postgres;