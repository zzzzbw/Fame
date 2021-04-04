package com.zbw.fame.listener;

import com.zbw.fame.exception.TipException;
import com.zbw.fame.listener.event.LogEvent;
import com.zbw.fame.model.entity.BaseEntity;
import com.zbw.fame.model.entity.SysLog;
import com.zbw.fame.model.enums.LogAction;
import com.zbw.fame.service.SysLogService;
import com.zbw.fame.util.FameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author by zzzzbw
 * @since 2021/01/22 14:40
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LogEventListener {


    private final SysLogService sysLogService;


    @Async
    @EventListener
    public void onLogEvent(LogEvent event) {
        log.info("onLogEvent event:{}", event);

        SysLog sysLog = createLog(event);
        sysLogService.save(sysLog);
    }

    private SysLog createLog(LogEvent event) {
        SysLog sysLog = new SysLog();
        LogAction action = event.getAction();
        String logData = createLogData(event.getData());
        sysLog.setData(logData);
        sysLog.setMessage(action.getMsg());
        sysLog.setLogType(event.getType());
        sysLog.setIp(event.getIp());
        sysLog.setUserId(event.getUserId());
        return sysLog;
    }

    private String createLogData(Object data) {
        if (Objects.isNull(data)) {
            return String.valueOf((Object) null);
        }
        if (data instanceof String) {
            // 字符串直接返回
            return (String) data;
        } else if (data instanceof Collection) {
            // 如果数据是集合
            if (ObjectUtils.isEmpty(data)) {
                throw new TipException("Log data is empty");
            }
            // 获取集合中的数据类型
            Object next = ((Collection<?>) data).iterator().next();
            if (next instanceof BaseEntity) {
                // 数据为拼接id字符串
                @SuppressWarnings("unchecked")
                Collection<? extends BaseEntity> list = (Collection<? extends BaseEntity>) data;
                return "ids:" + list.stream().map(BaseEntity::getId).map(String::valueOf).collect(Collectors.joining(","));
            }
        }

        return FameUtils.objectToJson(data);
    }

}
