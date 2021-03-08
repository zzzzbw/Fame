package com.zbw.fame.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zbw.fame.model.entity.SysLog;
import com.zbw.fame.model.enums.LogType;
import org.springframework.lang.NonNull;

/**
 * 日志 Service 接口
 *
 * @author zzzzbw
 * @since 2017/10/11 10:42
 */
public interface SysLogService extends IService<SysLog> {

    /**
     * 保存操作日志
     *
     * @param data    日志数据
     * @param message 日志信息
     * @param type    日志类型
     * @param ip      操作人ip
     * @param userId  操作人id
     */
    void save(String data, String message, LogType type, String ip, Integer userId);

    /**
     * 获取日志
     *
     * @param current  当前分页
     * @param size 分页大小
     * @return Page<Log>
     */
    Page<SysLog> pageSysLog(Integer current, Integer size);
}
