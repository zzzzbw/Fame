create table if not exists CATEGORY
(
    ID       INTEGER auto_increment
        primary key,
    NAME     VARCHAR(255)                        not null,
    PARENT_ID INT,
    CREATED  TIMESTAMP default CURRENT_TIMESTAMP not null,
    MODIFIED TIMESTAMP default CURRENT_TIMESTAMP not null,
    DELETED      TINYINT default 0 not null
);