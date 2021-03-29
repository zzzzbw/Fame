create table if not exists SYS_OPTION
(
    ID           INTEGER auto_increment
        primary key,
    OPTION_KEY   VARCHAR(100)                        not null
        unique,
    OPTION_VALUE VARCHAR(1023)                       not null,
    CREATED      TIMESTAMP default CURRENT_TIMESTAMP not null,
    MODIFIED     TIMESTAMP default CURRENT_TIMESTAMP not null,
    DELETED      TINYINT default 0 not null
);