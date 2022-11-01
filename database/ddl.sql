create table expenditure
(
    id         bigint       not null
        primary key,
    created_at datetime(6)  null,
    email      varchar(255) null,
    is_deleted bit          not null,
    memo       varchar(255) null,
    money      bigint       not null,
    updated_at datetime(6)  null
);

create index expenditure__email_is_deleted__idx
    on expenditure (email, is_deleted);

create table refresh_token
(
    email varchar(255) not null
        primary key,
    token varchar(255) null
);

create table user
(
    email    varchar(255) not null
        primary key,
    password varchar(255) null
);
