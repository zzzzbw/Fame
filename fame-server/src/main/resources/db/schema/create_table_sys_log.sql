create table if not exists SYS_LOG
(
    ID       INTEGER auto_increment
        primary key,
    DATA     TEXT,
    MESSAGE  VARCHAR(255),
    TYPE     VARCHAR(255),
    IP       VARCHAR(255),
    USER_ID  INT,
    CREATED  TIMESTAMP default CURRENT_TIMESTAMP not null,
    MODIFIED TIMESTAMP default CURRENT_TIMESTAMP not null,
    DELETED      TINYINT default 0 not null
);