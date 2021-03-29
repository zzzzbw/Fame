create table if not exists COMMENT
(
    ID         INT auto_increment
        primary key comment 'ID',
    ARTICLE_ID INT                                  not null comment '文章ID',
    PARENT_ID  INT comment '上级评论ID',
    CONTENT    TEXT                                 not null comment '内容',
    NAME       VARCHAR(255) comment '评论者名称',
    EMAIL      VARCHAR(255) comment '评论者邮箱',
    WEBSITE    VARCHAR(255) comment '评论者网址',
    AGREE      INT        default 0                 not null comment '赞同数量',
    DISAGREE   INT        default 0                 not null comment '反对数量',
    IP         VARCHAR(255) comment '评论者IP',
    AGENT      VARCHAR(255) comment '评论者浏览器AGENT',

    DELETED    TINYINT(1) default 0                 not null comment '逻辑删除 0:未删除 1:已删除',
    CREATED    TIMESTAMP  default CURRENT_TIMESTAMP not null comment '创建时间',
    MODIFIED   TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
) comment '评论表' charset = utf8;
