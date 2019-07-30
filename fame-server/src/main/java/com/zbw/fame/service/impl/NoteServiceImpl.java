package com.zbw.fame.service.impl;

import com.zbw.fame.exception.TipException;
import com.zbw.fame.model.domain.Note;
import com.zbw.fame.model.dto.NoteInfo;
import com.zbw.fame.model.enums.ArticleStatus;
import com.zbw.fame.repository.NoteRepository;
import com.zbw.fame.service.CommentService;
import com.zbw.fame.service.NoteService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.FameUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zbw
 * @since 2019/7/21 20:01
 */
@Slf4j
@Service
public class NoteServiceImpl extends AbstractArticleServiceImpl<Note> implements NoteService {


    private final CommentService commentService;

    public NoteServiceImpl(NoteRepository noteRepository,
                           CommentService commentService) {
        super(noteRepository);
        this.commentService = commentService;
    }


    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'front_notes'")
    @Override
    public List<NoteInfo> getFrontNoteList() {
        List<Note> noteList = articleRepository.findAllByStatus(ArticleStatus.PUBLISH, FameUtil.sortDescBy("priority", "id"));
        return noteList.stream().map(NoteInfo::new).collect(Collectors.toList());
    }


    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    @Override
    public Integer save(Note note) {
        if (null == note) {
            throw new TipException("自定义页面对象为空");
        }
        if (StringUtils.isEmpty(note.getTitle())) {
            throw new TipException("自定义页面标题不能为空");
        }
        if (note.getTitle().length() > FameConsts.MAX_TITLE_COUNT) {
            throw new TipException("自定义页面标题字数不能超过" + FameConsts.MAX_TITLE_COUNT);
        }

        if (StringUtils.isEmpty(note.getContent())) {
            throw new TipException("自定义页面内容不能为空");
        }
        if (note.getContent().length() > FameConsts.MAX_CONTENT_COUNT) {
            throw new TipException("自定义页面容字数不能超过" + FameConsts.MAX_CONTENT_COUNT);
        }
        if (null == note.getAuthorId()) {
            throw new TipException("请先登陆");
        }


        if (null != note.getId()) {
            Note oldNote = articleRepository.findById(note.getId())
                    .orElseThrow(() -> new TipException("修改文章id不存在"));

            FameUtil.copyPropertiesIgnoreNull(note, oldNote);
            articleRepository.saveAndFlush(oldNote);
        } else {
            articleRepository.saveAndFlush(note);
        }

        return note.getId();
    }


    @Transactional(rollbackFor = Throwable.class)
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    @Override
    public boolean delete(Integer id) {
        Note note = articleRepository.findById(id)
                .orElseThrow(() -> new TipException("没有id为" + id + "的文章"));
        note.setStatus(ArticleStatus.DELETE);
        if (articleRepository.save(note) != null) {
            log.info("删除页面: {}", note);
            int commentsResult = commentService.deleteCommentByArticleId(id);
            log.info("删除对应的评论,数量: {}", commentsResult);
            return true;
        }
        return false;
    }
}
