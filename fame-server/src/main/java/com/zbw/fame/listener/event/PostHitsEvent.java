package com.zbw.fame.listener.event;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;

/**
 * @author by zzzzbw
 * @since 2021/01/14 14:49
 */

@ToString
@Getter
public class PostHitsEvent extends ApplicationEvent {

    private final Integer postId;

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     * @param postId 文章id
     */
    public PostHitsEvent(Object source, @NonNull Integer postId) {
        super(source);
        this.postId = postId;
    }
}
