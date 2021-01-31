create table if not exists COMMENT
(
    ID         INTEGER auto_increment
        primary key,
    ARTICLE_ID INT                                 not null,
    PARENT_ID  INT,
    CONTENT    TEXT                                not null,
    NAME       VARCHAR(255),
    EMAIL      VARCHAR(255),
    WEBSITE    VARCHAR(255),
    AGREE      INT       default 0                 not null,
    DISAGREE   INT       default 0                 not null,
    IP         VARCHAR(255),
    AGENT      VARCHAR(255),
    CREATED    TIMESTAMP default CURRENT_TIMESTAMP not null,
    STATUS     VARCHAR(32),
    MODIFIED   TIMESTAMP default CURRENT_TIMESTAMP not null
);
