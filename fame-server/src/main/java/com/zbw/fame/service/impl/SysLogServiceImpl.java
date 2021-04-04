package com.zbw.fame.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbw.fame.mapper.SysLogMapper;
import com.zbw.fame.model.entity.SysLog;
import com.zbw.fame.model.enums.LogType;
import com.zbw.fame.service.SysLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志 Service 实现类
 *
 * @author zzzzbw
 * @since 2017/10/11 10:42
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements SysLogService {


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void save(String data, String message, LogType type, String ip, Integer userId) {
        SysLog sysLog = new SysLog();
        sysLog.setData(data);
        sysLog.setMessage(message);
        sysLog.setLogType(type);
        sysLog.setIp(ip);
        sysLog.setUserId(userId);
        this.save(sysLog);
    }

    @Override
    public Page<SysLog> pageSysLog(Integer current, Integer size) {
        Page<SysLog> page = new Page<>(current, size);
        return page(page);
    }

}
