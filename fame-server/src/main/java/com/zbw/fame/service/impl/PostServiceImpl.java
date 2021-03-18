package com.zbw.fame.service.impl;

import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.listener.event.LogEvent;
import com.zbw.fame.listener.event.ArticleHitsEvent;
import com.zbw.fame.model.domain.Post;
import com.zbw.fame.model.dto.Archive;
import com.zbw.fame.model.dto.PostInfo;
import com.zbw.fame.model.enums.ArticleStatus;
import com.zbw.fame.model.enums.LogAction;
import com.zbw.fame.model.enums.LogType;
import com.zbw.fame.repository.ArticleRepository;
import com.zbw.fame.service.*;
import com.zbw.fame.util.FameUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author zzzzbw
 * @since 2019/7/21 20:01
 */
@Slf4j
@Service
public class PostServiceImpl extends AbstractArticleServiceImpl<Post> implements PostService {

    private final CategoryService categoryService;

    private final TagService tagService;

    private final CommentService commentService;

    private final ApplicationEventPublisher eventPublisher;

    public PostServiceImpl(ArticleRepository<Post> articleRepository,
                           SysOptionService sysOptionService,
                           CategoryService categoryService,
                           TagService tagService,
                           CommentService commentService,
                           ApplicationEventPublisher eventPublisher) {
        super(articleRepository, sysOptionService);
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.commentService = commentService;
        this.eventPublisher = eventPublisher;
    }


    @Transactional(rollbackFor = Throwable.class)
    @Override
    public Integer save(Post post) {

        if (null != post.getId()) {
            Post oldPost = articleRepository.findById(post.getId())
                    .orElseThrow(() -> new NotFoundException(Post.class));

            FameUtils.copyProperties(post, oldPost, true);
            articleRepository.saveAndFlush(oldPost);
        } else {
            articleRepository.saveAndFlush(post);

            LogEvent logEvent = new LogEvent(this, post, LogAction.ADD, LogType.POST, FameUtils.getIp(), FameUtils.getLoginUser().getId());
            eventPublisher.publishEvent(logEvent);
        }

        Integer id = post.getId();
        //存储分类和标签
        categoryService.saveOrRemoveMetas(post.getCategory(), id);
        tagService.saveOrRemoveMetas(post.getTags(), id);
        return id;
    }


    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void delete(Integer id) {
        Post post = articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Post.class));
        post.setStatus(ArticleStatus.DELETE);

        log.info("删除文章: {}", post);
        articleRepository.save(post);

        int commentsResult = commentService.deleteCommentByArticleId(id);
        log.info("删除对应的评论,数量: {}", commentsResult);

        // 传空的属性，则移除该文章关联的属性
        categoryService.saveOrRemoveMetas("", post.getId());
        tagService.saveOrRemoveMetas("", post.getId());

        LogEvent logEvent = new LogEvent(this, post, LogAction.DELETE, LogType.POST, FameUtils.getIp(), FameUtils.getLoginUser().getId());
        eventPublisher.publishEvent(logEvent);

    }

    @Override
    public void visitPost(Integer postId) {
        eventPublisher.publishEvent(new ArticleHitsEvent(this, postId));
    }

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void increaseHits(Integer postId, Integer hits) {

        int update = articleRepository.increaseHits(postId, hits);
        if (update < 1) {
            log.info("post increaseHits fail! postId: {}", postId);
            throw new TipException("文章更新点击量失败");
        }
    }

    @Override
    public List<Archive> getArchives() {
        List<Post> posts = articleRepository.findAllByStatus(ArticleStatus.PUBLISH, FameUtils.sortDescById());
        List<Archive> archives = new ArrayList<>();
        String current = "";
        Calendar cal = Calendar.getInstance();
        for (Post post : posts) {
            cal.setTime(post.getCreated());
            String year = String.valueOf(cal.get(Calendar.YEAR));
            if (year.equals(current)) {
                Archive archive = archives.get(archives.size() - 1);
                archive.getPostInfos().add(new PostInfo(post));
            } else {
                current = year;
                Archive arc = new Archive();
                arc.setYear(year);
                List<PostInfo> pts = new ArrayList<>();
                pts.add(new PostInfo(post));
                arc.setPostInfos(pts);
                archives.add(arc);
            }
        }
        return archives;
    }
}
