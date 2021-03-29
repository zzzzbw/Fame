create table if not exists MIDDLE
(
    ID         INTEGER auto_increment
        primary key,
    ARTICLE_ID INT                                 not null,
    META_ID    INT                                 not null,
    CREATED    TIMESTAMP default CURRENT_TIMESTAMP not null,
    MODIFIED   TIMESTAMP default CURRENT_TIMESTAMP not null
);