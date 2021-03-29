create table if not exists TAG
(
    ID       INTEGER auto_increment
        primary key,
    NAME     VARCHAR(255)                        not null,
    CREATED  TIMESTAMP default CURRENT_TIMESTAMP not null,
    MODIFIED TIMESTAMP default CURRENT_TIMESTAMP not null,
    DELETED      TINYINT default 0 not null

);