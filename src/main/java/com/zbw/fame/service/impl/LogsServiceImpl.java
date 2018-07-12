package com.zbw.fame.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zbw.fame.mapper.LogsMapper;
import com.zbw.fame.model.Logs;
import com.zbw.fame.service.LogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志 Service 实现类
 *
 * @author zbw
 * @create 2017/10/11 10:42
 */
@Service("logsService")
@Transactional(rollbackFor = Throwable.class)
public class LogsServiceImpl implements LogsService {

    @Autowired
    private LogsMapper logsMapper;

    @Override
    public void save(String action, String data, String message, String type) {
        this.save(action, data, message, type, null, null);
    }

    @Override
    public void save(String action, String data, String message, String type, String ip) {
        this.save(action, data, message, type, ip, null);
    }

    @Override
    public void save(String action, String data, String message, String type, String ip, Integer userId) {
        Logs log = new Logs();
        log.setAction(action);
        log.setData(data);
        log.setMessage(message);
        log.setType(type);
        log.setIp(ip);
        log.setUserId(userId);
        logsMapper.insert(log);
    }

    @Override
    public Page<Logs> getLogs(Integer page, Integer limit) {
        return PageHelper.startPage(page, limit).doSelectPage(() -> logsMapper.selectAll());
    }

}
