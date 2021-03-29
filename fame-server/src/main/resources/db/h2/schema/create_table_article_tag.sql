create table if not exists ARTICLE_TAG
(
    ID         INTEGER auto_increment
        primary key,
    TAG_ID     INT                                 NOT NULL,
    ARTICLE_ID INT                                 NOT NULL,
    CREATED    TIMESTAMP default CURRENT_TIMESTAMP not null,
    MODIFIED   TIMESTAMP default CURRENT_TIMESTAMP not null,
    DELETED    TINYINT   default 0                 not null
);