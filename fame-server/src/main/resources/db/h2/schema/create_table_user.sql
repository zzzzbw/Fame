create table if not exists USER
(
    ID           INTEGER auto_increment
        primary key,
    USERNAME     VARCHAR(45)                         not null
        unique,
    PASSWORD_MD5 VARCHAR(45)                         not null,
    EMAIL        VARCHAR(45),
    SCREEN_NAME  VARCHAR(45),
    CREATED      TIMESTAMP default CURRENT_TIMESTAMP not null,
    LOGGED       TIMESTAMP default CURRENT_TIMESTAMP not null,
    MODIFIED     TIMESTAMP default CURRENT_TIMESTAMP not null,
    DELETED      TINYINT default 0 not null
);