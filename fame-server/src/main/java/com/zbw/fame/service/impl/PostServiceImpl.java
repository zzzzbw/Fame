package com.zbw.fame.service.impl;

import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.model.domain.Post;
import com.zbw.fame.model.dto.Archive;
import com.zbw.fame.model.dto.PostInfo;
import com.zbw.fame.model.enums.ArticleStatus;
import com.zbw.fame.model.enums.LogType;
import com.zbw.fame.repository.ArticleRepository;
import com.zbw.fame.service.*;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.FameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @author zbw
 * @since 2019/7/21 20:01
 */
@Slf4j
@Service
public class PostServiceImpl extends AbstractArticleServiceImpl<Post> implements PostService {

    private final CategoryService categoryService;

    private final TagService tagService;

    private final CommentService commentService;

    private final LogService logService;

    private static String LOG_MESSAGE_CREATE_POST = "新建文章";
    private static String LOG_MESSAGE_DELETE_POST = "删除文章";

    public PostServiceImpl(ArticleRepository<Post> articleRepository,
                           OptionService optionService,
                           CategoryService categoryService,
                           TagService tagService,
                           CommentService commentService,
                           LogService logService) {
        super(articleRepository, optionService);
        this.categoryService = categoryService;
        this.tagService = tagService;
        this.commentService = commentService;
        this.logService = logService;
    }


    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    @Override
    public Integer save(Post post) {
        if (null == post) {
            throw new TipException("文章对象为空");
        }
        if (StringUtils.isEmpty(post.getTitle())) {
            throw new TipException("文章标题不能为空");
        }
        if (post.getTitle().length() > FameConsts.MAX_TITLE_COUNT) {
            throw new TipException("文章标题字数不能超过" + FameConsts.MAX_TITLE_COUNT);
        }

        if (StringUtils.isEmpty(post.getContent())) {
            throw new TipException("文章内容不能为空");
        }
        if (post.getContent().length() > FameConsts.MAX_CONTENT_COUNT) {
            throw new TipException("文章内容字数不能超过" + FameConsts.MAX_CONTENT_COUNT);
        }
        if (null == post.getAuthorId()) {
            throw new TipException("请先登陆后发布文章");
        }

        if (null != post.getId()) {
            Post oldPost = articleRepository.findById(post.getId())
                    .orElseThrow(() -> new NotFoundException(Post.class));

            FameUtil.copyPropertiesIgnoreNull(post, oldPost);
            articleRepository.saveAndFlush(oldPost);
        } else {
            articleRepository.saveAndFlush(post);
            logService.save(post.toString(), LOG_MESSAGE_CREATE_POST, LogType.POST);
        }

        Integer id = post.getId();
        //存储分类和标签
        categoryService.saveOrRemoveMetas(post.getCategory(), id);
        tagService.saveOrRemoveMetas(post.getTags(), id);
        return id;
    }


    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    @Override
    public void delete(Integer id) {
        Post post = articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Post.class));
        post.setStatus(ArticleStatus.DELETE);

        log.info("删除文章: {}", post);
        articleRepository.save(post);
        logService.save(post.toString(), LOG_MESSAGE_DELETE_POST, LogType.POST);


        int commentsResult = commentService.deleteCommentByArticleId(id);
        log.info("删除对应的评论,数量: {}", commentsResult);

        // 传空的属性，则移除该文章关联的属性
        categoryService.saveOrRemoveMetas("", post.getId());
        tagService.saveOrRemoveMetas("", post.getId());

    }

    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    @Override
    public boolean updateHits(Integer postId, Integer hits) {
        Post post = articleRepository.findById(postId)
                .orElseThrow(() -> new TipException("文章不能为空"));
        post.setHits(hits);
        return articleRepository.saveAndFlush(post) != null;
    }

    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'font_archives'")
    @Override
    public List<Archive> getArchives() {
        List<Post> posts = articleRepository.findAllByStatus(ArticleStatus.PUBLISH, FameUtil.sortDescById());
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
