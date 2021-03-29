create table if not exists ARTICLE_CATEGORY
(
    ID          INT auto_increment
        primary key comment 'ID',
    ARTICLE_ID  INT                                  not null comment '文章ID',
    CATEGORY_ID INT                                  not null comment '分类ID',
    DELETED     TINYINT(1) default 0                 not null comment '逻辑删除 0:未删除 1:已删除',
    CREATED     TIMESTAMP  default CURRENT_TIMESTAMP not null comment '创建时间',
    MODIFIED    TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
) comment '文章-分类关联表' charset = utf8;