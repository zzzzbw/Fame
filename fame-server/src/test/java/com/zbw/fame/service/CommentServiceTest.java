package com.zbw.fame.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zbw.fame.model.dto.LoginUser;
import com.zbw.fame.model.dto.Pagination;
import com.zbw.fame.BaseTest;
import com.zbw.fame.model.entity.Comment;
import com.zbw.fame.util.FameUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


/**
 * @author zzzzbw
 * @since 2019/6/24 14:12
 */
@Slf4j
public class CommentServiceTest extends BaseTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private EmailService emailService;

    @Test
    public void createComment() {
        Comment comment = new Comment();
        comment.setContent("test comment");
        comment.setName("test");
        comment.setArticleId(2);
        commentService.createComment(comment);
    }

    @Test
    public void test2() {
        Page<Comment> result = commentService.pageByArticleId(1, 10, 1);
        log.info("{}", Pagination.of(result));
    }

    @Test
    public void test3() {
        log.info("{}", commentService.getCommentDto(2));
    }

    @Test
    public void test4() {
        commentService.deleteComment(2);
    }

    @Test
    public void test5() {
        log.info("{}", commentService.count());
    }

    @Test
    public void test6() throws InterruptedException {
        Comment comment = new Comment();
        comment.setName("zzzzbw");
        comment.setContent("测试邮件内容");
        comment.setArticleId(1);

        emailService.sendEmailToAdmin(comment);
        Thread.sleep(2000);
    }

    @Test
    public void newComment() throws InterruptedException {
        commentService.createCommentEvent(new Comment());
        Thread.sleep(2000);
    }

    @Test
    public void deleteComment() {
        LoginUser user = new LoginUser();
        user.setId(1);
        FameUtils.setLoginUser(user);

        commentService.deleteComment(1);
    }
}
