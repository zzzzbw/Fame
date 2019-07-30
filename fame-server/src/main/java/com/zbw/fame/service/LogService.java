package com.zbw.fame.service;

import com.zbw.fame.model.domain.Log;
import com.zbw.fame.model.enums.LogType;
import org.springframework.data.domain.Page;

/**
 * 日志 Service 接口
 *
 * @author zbw
 * @since 2017/10/11 10:42
 */
public interface LogService {

    /**
     * 保存操作日志
     *
     * @param data    日志数据
     * @param message 日志信息
     * @param type    日志类型
     */
    void save(String data, String message, LogType type);

    /**
     * 保存操作日志
     *
     * @param data    日志数据
     * @param message 日志信息
     * @param type    日志类型
     * @param ip      操作人ip
     */
    void save(String data, String message, LogType type, String ip);

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
     * @param page  当前分页
     * @param limit 分页大小
     * @return Page<Log>
     */
    Page<Log> getLogs(Integer page, Integer limit);
}
