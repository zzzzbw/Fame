package com.zbw.fame.service;

import com.github.pagehelper.Page;
import com.zbw.fame.model.Logs;

/**
 * 日志 Service 接口
 *
 * @author zbw
 * @since 2017/10/11 10:42
 */
public interface LogsService {

    /**
     * 保存操作日志
     *
     * @param action  日志动作
     * @param data    日志数据
     * @param message 日志信息
     * @param type    日志类型
     */
    void save(String action, String data, String message, String type);

    /**
     * 保存操作日志
     *
     * @param action  日志动作
     * @param data    日志数据
     * @param message 日志信息
     * @param type    日志类型
     * @param ip      操作人ip
     */
    void save(String action, String data, String message, String type, String ip);

    /**
     * 保存操作日志
     *
     * @param action  日志动作
     * @param data    日志数据
     * @param message 日志信息
     * @param type    日志类型
     * @param ip      操作人ip
     * @param userId  操作人id
     */
    void save(String action, String data, String message, String type, String ip, Integer userId);

    /**
     * 获取日志
     *
     * @param page  当前分页
     * @param limit 分页大小
     * @return Page<Logs>
     */
    Page<Logs> getLogs(Integer page, Integer limit);
}
