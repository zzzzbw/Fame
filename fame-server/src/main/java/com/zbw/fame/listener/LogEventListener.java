package com.zbw.fame.listener;

import com.zbw.fame.listener.event.LogEvent;
import com.zbw.fame.model.domain.Log;
import com.zbw.fame.model.enums.LogType;
import com.zbw.fame.service.LogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author by zzzzbw
 * @since 2021/01/22 14:40
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LogEventListener {


    private final LogService logService;


    @Async
    @EventListener
    public void onLogEvent(LogEvent event) {
        log.info("onLogEvent event:{}", event);

        Log systemLog;
        switch (event.getType()) {
            case COMMENT:
                systemLog = createCommentLog(event);
        }


        // logService.save(log);
    }

    private Log createCommentLog(LogEvent event) {
        Log systemLog = new Log();
        switch (event.getAction()) {
            case ADD:
                systemLog.setData(event.getData().toString());
                systemLog.setMessage("");
                systemLog.setType(event.getType());
                systemLog.setIp(event.getIp());
                systemLog.setUserId(event.getUserId());
        }
        return systemLog;
    }
}
