package com.zbw.fame.service.impl;

import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.listener.event.LogEvent;
import com.zbw.fame.model.domain.Note;
import com.zbw.fame.model.dto.NoteInfo;
import com.zbw.fame.model.enums.ArticleStatus;
import com.zbw.fame.model.enums.LogAction;
import com.zbw.fame.model.enums.LogType;
import com.zbw.fame.repository.NoteRepository;
import com.zbw.fame.service.CommentService;
import com.zbw.fame.service.NoteService;
import com.zbw.fame.service.OptionService;
import com.zbw.fame.util.FameUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zzzzbw
 * @since 2019/7/21 20:01
 */
@Slf4j
@Service
public class NoteServiceImpl extends AbstractArticleServiceImpl<Note> implements NoteService {


    private final CommentService commentService;

    private final ApplicationEventPublisher eventPublisher;

    public NoteServiceImpl(NoteRepository noteRepository,
                           OptionService optionService,
                           CommentService commentService,
                           ApplicationEventPublisher eventPublisher) {
        super(noteRepository, optionService);
        this.commentService = commentService;
        this.eventPublisher = eventPublisher;
    }


    @Override
    public List<NoteInfo> getFrontNoteList() {
        List<Note> noteList = articleRepository.findAllByStatus(ArticleStatus.PUBLISH, FameUtils.sortDescBy("priority", "id"));
        return noteList.stream().map(NoteInfo::new).collect(Collectors.toList());
    }


    @Transactional(rollbackFor = Throwable.class)
    @Override
    public Integer save(@NonNull Note note) {
        if (null != note.getId()) {
            Note oldNote = articleRepository.findById(note.getId())
                    .orElseThrow(() -> new NotFoundException(Note.class));

            FameUtils.copyPropertiesIgnoreNull(note, oldNote);
            articleRepository.saveAndFlush(oldNote);
        } else {
            articleRepository.saveAndFlush(note);

            LogEvent logEvent = new LogEvent(this, note, LogAction.ADD, LogType.NOTE, FameUtils.getIp(), FameUtils.getLoginUser().getId());
            eventPublisher.publishEvent(logEvent);
        }

        return note.getId();
    }


    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void delete(Integer id) {
        Note note = articleRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Note.class));

        log.info("删除页面: {}", note);
        articleRepository.save(note);

        int commentsResult = commentService.deleteCommentByArticleId(id);
        log.info("删除对应的评论,数量: {}", commentsResult);

        LogEvent logEvent = new LogEvent(this, note, LogAction.DELETE, LogType.NOTE, FameUtils.getIp(), FameUtils.getLoginUser().getId());
        eventPublisher.publishEvent(logEvent);
    }
}
