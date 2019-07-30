package com.zbw.fame.model.domain;

import com.zbw.fame.model.enums.LogType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;

/**
 * 日志 Model
 *
 * @author zbw
 * @since 2017/10/11 9:57
 */
@Entity
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Data
public class Log extends BaseEntity {

    /**
     * 操作数据
     */
    @Column(name = "data", columnDefinition = "TEXT")
    private String data;

    /**
     * 操作信息
     */
    @Column(name = "message", columnDefinition = "VARCHAR(255)")
    private String message;

    /**
     * 操作类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "VARCHAR(255)")
    private LogType type;

    /**
     * ip地址
     */
    @Column(name = "ip", columnDefinition = "VARCHAR(255)")
    private String ip;

    /**
     * 操作人
     */
    @Column(name = "user_id", columnDefinition = "INT")
    private Integer userId;
}
