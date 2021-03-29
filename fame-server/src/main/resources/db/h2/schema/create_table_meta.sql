create table if not exists META
(
    ID       INTEGER auto_increment
        primary key,
    NAME     VARCHAR(255)                        not null,
    TYPE     VARCHAR(45)                         not null,
    CREATED  TIMESTAMP default CURRENT_TIMESTAMP not null,
    MODIFIED TIMESTAMP default CURRENT_TIMESTAMP not null
);