package com.zbw.fame.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zbw.fame.mapper.LogMapper;
import com.zbw.fame.model.domain.Log;
import com.zbw.fame.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志 Service 实现类
 *
 * @author zbw
 * @since 2017/10/11 10:42
 */
@Service("logsService")
@Transactional(rollbackFor = Throwable.class)
public class LogServiceImpl implements LogService {

    @Autowired
    private LogMapper logMapper;

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
        Log log = new Log();
        log.setAction(action);
        log.setData(data);
        log.setMessage(message);
        log.setType(type);
        log.setIp(ip);
        log.setUserId(userId);
        logMapper.insertSelective(log);
    }

    @Override
    public Page<Log> getLogs(Integer page, Integer limit) {
        return PageHelper.startPage(page, limit).doSelectPage(() -> logMapper.selectAll());
    }

}
