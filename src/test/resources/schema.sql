create table if not exists clients
(
    id             uuid         not null
        constraint client_pk
            primary key,
    identification varchar(50)  not null,
    name           varchar(256) not null,
    address        varchar(256) not null,
    phone_number   varchar(50)  not null,
    gender         varchar(50)  not null,
    birthdate      date         not null,
    password       varchar(512) not null,
    state          varchar(50)  not null
);

create unique index if not exists client_identification_uidx
    on clients (identification);

create table if not exists accounts
(
    id                uuid          not null
        constraint account_pk
            primary key,
    client_id         uuid          not null
        constraint account_client_id_fk
            references clients on delete cascade,
    account_number    varchar(50)   not null,
    account_type      varchar(50)   not null,
    balance_available numeric(9, 2) not null,
    state             varchar(50)   not null
);

create index if not exists account_client_id_idx
    on accounts (client_id);

create unique index if not exists account_number_uidx
    on accounts (account_number);

create table if not exists movements
(
    id                uuid          not null
        constraint movement_pk
            primary key,
    account_id        uuid          not null
        constraint movement_account_id_fk
            references accounts on delete cascade,
    movement_date     timestamp     not null,
    movement_type     varchar(50)   not null,
    movement_value    numeric(9, 2) not null,
    initial_balance   numeric(9, 2) not null,
    balance_available numeric(9, 2) not null,
    state             varchar(50)   not null
);

create index if not exists movement_account_id_idx
    on movements (account_id, movement_date DESC);

create index if not exists movement_type_idx
    on movements (account_id, movement_type, movement_date DESC);
