create table if not exists USER
(
    ID           INT auto_increment
        primary key comment 'ID',
    USERNAME     VARCHAR(45)                          not null
        unique comment '用户名',
    PASSWORD_MD5 VARCHAR(45)                          not null comment '密码MD5',
    EMAIL        VARCHAR(45) comment '邮箱',
    SCREEN_NAME  VARCHAR(45) comment '显示名称',
    LOGGED       TIMESTAMP  default CURRENT_TIMESTAMP not null comment '最后登录时间',

    DELETED      TINYINT(1) default 0                 not null comment '逻辑删除 0:未删除 1:已删除',
    CREATED      TIMESTAMP  default CURRENT_TIMESTAMP not null comment '创建时间',
    MODIFIED     TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
) comment '用户表' charset = utf8;