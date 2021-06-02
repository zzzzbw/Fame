package com.zbw.fame.listener.event;

import com.zbw.fame.model.dto.CommentDto;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * @author by zzzzbw
 * @since 2021/01/14 17:18
 */
@ToString
@Getter
public class CommentNewEvent extends ApplicationEvent {

    private final CommentDto commentDto;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param commentDto 评论详情
     */
    public CommentNewEvent(Object source, CommentDto commentDto) {
        super(source);
        this.commentDto = commentDto;
    }
}
