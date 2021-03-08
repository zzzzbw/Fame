package com.zbw.fame.listener;

import com.zbw.fame.listener.event.CommentNewEvent;
import com.zbw.fame.model.dto.CommentDto;
import com.zbw.fame.service.CommentService;
import com.zbw.fame.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @author by zzzzbw
 * @since 2021/01/14 17:19
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class CommentNewEventListener {

    private final CommentService commentService;

    private final EmailService emailService;

    @Async
    @EventListener
    public void onCommentNewEvent(CommentNewEvent event) {
        log.info("onCommentNewEvent event:{}", event);

        //发送邮件提醒
        CommentDto commentDetail = commentService.getCommentDto(event.getCommentId());
        // 发送给管理员
        emailService.sendEmailToAdmin(commentDetail);
        if (null != commentDetail.getParentComment() && !StringUtils.hasText(commentDetail.getParentComment().getEmail())) {
            // 发送给回复
            emailService.sendEmailToUser(commentDetail, commentDetail.getParentComment().getEmail());
        }
    }
}
