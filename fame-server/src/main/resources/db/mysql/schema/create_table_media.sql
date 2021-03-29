create table if not exists MEDIA
(
    ID        INT auto_increment
        primary key comment 'ID',
    NAME      VARCHAR(255)                          not null comment '文件名',
    SUFFIX      VARCHAR(255)                          not null comment '文件后缀',
    THUMB_URL      VARCHAR(255)                          not null comment '缩略图路径',
    URL      VARCHAR(255)                          not null comment '文件路径',

    DELETED   TINYINT(1) default 0                 not null comment '逻辑删除 0:未删除 1:已删除',
    CREATED   TIMESTAMP  default CURRENT_TIMESTAMP not null comment '创建时间',
    MODIFIED  TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
) comment '媒体文件表' charset = utf8;