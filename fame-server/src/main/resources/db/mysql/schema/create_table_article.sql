create table if not exists ARTICLE
(
    ID            INT auto_increment
        primary key comment 'ID',
    TITLE         VARCHAR(255)                         not null comment '标题',
    CONTENT       MEDIUMTEXT                           not null comment '内容',
    AUTHOR_ID     INT                                  not null comment '作者ID',
    HITS          INT        default 0                 not null comment '点击量',
    STATUS        VARCHAR(32)                          not null comment '状态',
    LIST_SHOW     TINYINT(1) default 1                 not null comment '是否在列表显示',
    HEADER_SHOW   TINYINT(1) default 0                 not null comment '是否在顶部显示',
    ALLOW_COMMENT TINYINT(1) default 1                 not null comment '是否允许评论',
    PRIORITY      INT        default 0                 not null comment '优先级, 倒序',

    DELETED       TINYINT(1) default 0                 not null comment '逻辑删除 0:未删除 1:已删除',
    CREATED       TIMESTAMP  default CURRENT_TIMESTAMP not null comment '创建时间',
    MODIFIED      TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
) comment '文章表' charset = utf8;