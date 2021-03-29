create table if not exists MEDIA
(
    ID        INTEGER auto_increment
        primary key,
    CREATED   TIMESTAMP default CURRENT_TIMESTAMP not null,
    MODIFIED  TIMESTAMP default CURRENT_TIMESTAMP not null,
    NAME      VARCHAR(255)                        not null,
    SUFFIX    VARCHAR(255)                        not null,
    THUMB_URL VARCHAR(1023),
    URL       VARCHAR(1023)                       not null
);