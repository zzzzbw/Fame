create table if not exists SYS_LOG
(
    ID       INT auto_increment
        primary key comment 'ID',
    DATA     TEXT comment '操作数据',
    MESSAGE  VARCHAR(1023) comment '操作信息',
    LOG_TYPE VARCHAR(32)                          not null comment '操作类型',
    IP       varchar(255) comment 'ip地址',
    USER_ID  INT comment '操作人ID',

    DELETED  TINYINT(1) default 0                 not null comment '逻辑删除 0:未删除 1:已删除',
    CREATED  TIMESTAMP  default CURRENT_TIMESTAMP not null comment '创建时间',
    MODIFIED TIMESTAMP  default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '修改时间'
) comment '系统日志表' charset = utf8;