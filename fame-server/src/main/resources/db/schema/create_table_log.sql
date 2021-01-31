create table if not exists LOG
(
    ID       INTEGER auto_increment
        primary key,
    DATA     TEXT,
    MESSAGE  VARCHAR(255),
    TYPE     VARCHAR(255),
    IP       VARCHAR(255),
    USER_ID  INT,
    CREATED  TIMESTAMP default CURRENT_TIMESTAMP not null,
    MODIFIED TIMESTAMP default CURRENT_TIMESTAMP not null
);