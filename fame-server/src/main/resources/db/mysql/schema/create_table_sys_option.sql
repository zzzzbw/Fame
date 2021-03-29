create table if not exists SYS_OPTION
(
    ID           INT auto_increment
        primary key comment 'ID',
    OPTION_KEY   VARCHAR(255) comment '设置key',
    OPTION_VALUE VARCHAR(1023) comment '设置value',

    DELETED      TINYINT(1) default 0                 not null comment '逻辑删除 0:未删除 1:已删除',
    CREATED      TIMESTAMP  default CURRENT_TIMESTAMP not null comment '创建时间',
    MODIFIED     TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
) comment '文章表' charset = utf8;