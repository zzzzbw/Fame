-- auto-generated definition
create table if not exists  ARTICLE
(

    ID            INTEGER primary key auto_increment,
    TITLE         VARCHAR(255)                        not null,
    CREATED       TIMESTAMP default CURRENT_TIMESTAMP not null,
    MODIFIED      TIMESTAMP default CURRENT_TIMESTAMP not null,
    CONTENT       MEDIUMTEXT,
    AUTHOR_ID     INT,
    HITS          INT       default 0                 not null,
    TAGS          VARCHAR(500),
    CATEGORY      VARCHAR(500),
    STATUS        VARCHAR(32),
    TYPE          VARCHAR(45)                         not null,
    ALLOW_COMMENT BOOLEAN   default TRUE              not null,
    COMMENT_COUNT INT       default 0                 not null,
    PRIORITY      INT       default 0                 not null
);
