-- Для @GeneratedValue(strategy = GenerationType.IDENTITY)
create table client
(
    id   bigserial not null primary key,
    name varchar(50)
);

create table address
(
    id   bigserial not null primary key,
    street varchar(100),
    client_id int8 NOT NULL,
    CONSTRAINT address_fk FOREIGN KEY (client_id) REFERENCES client(id)
);

create table phone
(
    id   bigserial not null primary key,
    number varchar(25),
    client_id int8 NOT NULL,
    CONSTRAINT phone_fk FOREIGN KEY (client_id) REFERENCES client(id)
);



