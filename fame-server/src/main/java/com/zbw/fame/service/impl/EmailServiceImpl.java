package com.zbw.fame.service.impl;

import com.zbw.fame.listener.event.LogEvent;
import com.zbw.fame.model.entity.Comment;
import com.zbw.fame.model.enums.LogAction;
import com.zbw.fame.model.enums.LogType;
import com.zbw.fame.service.EmailService;
import com.zbw.fame.service.SysOptionService;
import com.zbw.fame.util.FameConst;
import com.zbw.fame.util.OptionKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 发送邮件 Service 实现类
 *
 * @author zzzzbw
 * @since 2018/4/9 15:52
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class EmailServiceImpl implements EmailService {

    private final SysOptionService sysOptionService;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    @Async
    public void sendEmailToAdmin(Comment comment) {
        if (!isEmail(comment.getEmail())) {
            return;
        }

        Map<String, String> params = getEmailParams(comment);
        String content = FameConst.getEmailTemplateAdminContent(params);

        String logData = content + ";  发送给管理员";
        log.info("sendEmailToAdmin start: {}", new Date());
        try {
            String emailUsername = sysOptionService.get(OptionKeys.EMAIL_USERNAME);
            sendEmail(content, emailUsername);

            LogEvent logEvent = new LogEvent(this, logData, LogAction.SUCCESS, LogType.EMAIL);
            eventPublisher.publishEvent(logEvent);
        } catch (Exception e) {
            LogEvent logEvent = new LogEvent(this, logData, LogAction.FAIL, LogType.EMAIL);
            eventPublisher.publishEvent(logEvent);
            log.error(e.getMessage(), e);
        }
    }

    @Override
    @Async
    public void sendEmailToUser(Comment comment, String replyEmail) {
        if (!isEmail(replyEmail)) {
            return;
        }

        Map<String, String> params = getEmailParams(comment);
        String content = FameConst.getEmailTemplateUserContent(params);

        String logData = content + ";  发送给:" + replyEmail;
        log.info("sendEmailToUser start: {}", new Date());
        try {
            sendEmail(content, replyEmail);

            LogEvent logEvent = new LogEvent(this, logData, LogAction.SUCCESS, LogType.EMAIL);
            eventPublisher.publishEvent(logEvent);
        } catch (Exception e) {
            LogEvent logEvent = new LogEvent(this, logData, LogAction.FAIL, LogType.EMAIL);
            eventPublisher.publishEvent(logEvent);
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 判定是否要发送该邮件
     *
     * @param email 收件人邮箱
     * @return 是否发送邮件
     */
    private boolean isEmail(String email) {
        boolean isEmail = sysOptionService.get(OptionKeys.IS_EMAIL, Boolean.FALSE);
        if (!isEmail) {
            return false;
        }

        String adminUserEmail = sysOptionService.get(OptionKeys.EMAIL_USERNAME, "");
        // 如果是管理员的回复则不必通知管理员
        return ObjectUtils.isEmpty(adminUserEmail) || !adminUserEmail.equals(email);
    }

    /**
     * 获取邮件Context
     *
     * @param comment 评论
     * @return {@see Context}
     */
    private Map<String, String> getEmailParams(Comment comment) {
        Map<String, String> params = new HashMap<>();

        String websiteName = sysOptionService.get(OptionKeys.BLOG_NAME);
        String website = sysOptionService.get(OptionKeys.BLOG_WEBSITE);

        // 如果网址最后没有/,则补上
        if (!ObjectUtils.isEmpty(website)
                && website.lastIndexOf("/") != website.length()) {
            website = website + "/";
        }

        params.put("websiteName", websiteName);
        params.put("website", website);
        params.put("name", comment.getName());
        params.put("content", comment.getContent());
        params.put("articleId", String.valueOf(comment.getArticleId()));
        return params;
    }

    /**
     * 发送邮件
     *
     * @param content 邮件内容(html)
     * @param to      收件人
     * @throws MessagingException
     */
    private void sendEmail(String content, String to) throws MessagingException {
        String subject = sysOptionService.get(OptionKeys.EMAIL_SUBJECT, FameConst.DEFAULT_EMAIL_TEMPLATE_SUBJECT);
        String host = sysOptionService.get(OptionKeys.EMAIL_HOST);
        Integer port = sysOptionService.get(OptionKeys.EMAIL_PORT, 25);
        String username = sysOptionService.get(OptionKeys.EMAIL_USERNAME);
        String password = sysOptionService.get(OptionKeys.EMAIL_PASSWORD);

        JavaMailSender mailSender = (JavaMailSender) mailSender(host, port,
                username, password);
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        helper.setFrom(username);
        helper.setTo(to);
        helper.setText(content, true);
        helper.setSubject(subject);
        mailSender.send(mimeMessage);
    }


    /**
     * 获取MailSender
     *
     * @param host     主机名
     * @param port     端口
     * @param username 邮件名
     * @param password 密码
     * @return MailSender
     */
    private MailSender mailSender(String host, Integer port, String username, String password) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);
        return mailSender;
    }
}
