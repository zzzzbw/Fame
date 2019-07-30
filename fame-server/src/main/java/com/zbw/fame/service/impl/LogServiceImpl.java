package com.zbw.fame.service.impl;

import com.zbw.fame.model.domain.Log;
import com.zbw.fame.model.enums.LogType;
import com.zbw.fame.repository.LogRepository;
import com.zbw.fame.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志 Service 实现类
 *
 * @author zbw
 * @since 2017/10/11 10:42
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void save(String data, String message, LogType type) {
        this.save(data, message, type, null, null);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void save(String data, String message, LogType type, String ip) {
        this.save(data, message, type, ip, null);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void save(String data, String message, LogType type, String ip, Integer userId) {
        Log log = new Log();
        log.setData(data);
        log.setMessage(message);
        log.setType(type);
        log.setIp(ip);
        log.setUserId(userId);
        logRepository.save(log);
    }

    @Override
    public Page<Log> getLogs(Integer page, Integer limit) {
        return logRepository.findAll(PageRequest.of(page, limit));
    }

}
