-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);
