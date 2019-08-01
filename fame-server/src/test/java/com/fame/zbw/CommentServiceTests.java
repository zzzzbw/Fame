package com.fame.zbw;

import com.zbw.fame.model.domain.Comment;
import com.zbw.fame.model.dto.Pagination;
import com.zbw.fame.service.CommentService;
import com.zbw.fame.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.mockito.exceptions.misusing.CannotVerifyStubOnlyMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

/**
 * @author zhangbowen
 * @since 2019/6/24 14:12
 */
@Slf4j
public class CommentServiceTests extends BaseTests {

    @Autowired
    private CommentService commentService;

    @Autowired
    private EmailService emailService;

    @Test
    public void test1() {
        Comment comment = new Comment();
        comment.setContent("test comment");
        comment.setName("test");
        comment.setArticleId(1);
        commentService.save(comment);
    }

    @Test
    public void test2() {
        Page<Comment> result = commentService.getCommentsByArticleId(1, 10, 1);
        log.info("{}", Pagination.of(result));
    }

    @Test
    public void test3() {
        log.info("{}", commentService.getCommentDetail(2));
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
}
