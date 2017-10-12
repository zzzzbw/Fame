package com.zbw.fame.service;

import com.zbw.fame.model.Logs;

import java.util.List;

/**
 * 日志 Service 接口
 *
 * @auther zbw
 * @create 2017/10/11 10:42
 */
public interface LogsService {

    void save(String action, String data, String message, String type);

    void save(String action, String data, String message, String type, String ip);

    void save(String action, String data, String message, String type, String ip, Integer user_id);

    List<Logs> getLogs(Integer page);

    Integer getVisit();
}
