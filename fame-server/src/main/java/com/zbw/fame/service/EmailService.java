package com.zbw.fame.service;

import com.zbw.fame.model.domain.Comments;

/**
 * 发送邮件 Service 接口
 *
 * @author zbw
 * @since 2018/4/9 15:51
 */
public interface EmailService {

    /**
     * 发送邮件给管理员
     *
     * @param comments 回复的Comment信息
     */
    void sendEmailToAdmin(Comments comments);

    /**
     * 发送邮件给被评论的用户
     *
     * @param comments   评论的Comment信息
     * @param replyEmail 被评论人邮箱
     */
    void sendEmailToUser(Comments comments, String replyEmail);
}
