package com.zbw.fame.listener.event;

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

    private final Integer commentId;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source    the object on which the event initially occurred or with
     *                  which the event is associated (never {@code null})
     * @param commentId 评论id
     */
    public CommentNewEvent(Object source, Integer commentId) {
        super(source);
        this.commentId = commentId;
    }
}
