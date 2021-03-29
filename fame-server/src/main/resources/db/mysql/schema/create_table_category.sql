create table if not exists CATEGORY
(
    ID        INT auto_increment
        primary key comment 'ID',
    PARENT_ID INT comment '上级分类ID',
    NAME      VARCHAR(32)                          not null comment '分类名称',
    DELETED   TINYINT(1) default 0                 not null comment '逻辑删除 0:未删除 1:已删除',
    CREATED   TIMESTAMP  default CURRENT_TIMESTAMP not null comment '创建时间',
    MODIFIED  TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
) comment '分类表' charset = utf8;