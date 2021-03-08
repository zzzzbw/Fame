package com.zbw.fame.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

/**
 * @author by zzzzbw
 * @since 2021/03/08 11:01
 */
@Data
public class BaseEntity {

    @TableId(type = IdType.AUTO)
    protected Integer id;

    protected Date created = new Date();

    @TableField(value = "modified", update = "now()")
    protected Date modified = new Date();

    /**
     * 逻辑删除 0:未删除 1: 已删除
     */
    @TableLogic
    protected Integer deleted = 0;
}
