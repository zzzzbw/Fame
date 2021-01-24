package com.zbw.fame.listener;

import com.zbw.fame.listener.event.PostHitsEvent;
import com.zbw.fame.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author by zzzzbw
 * @since 2021/01/14 14:48
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PostHitsEventListener {

    private final PostService postService;

    @Async
    @EventListener
    public void onPostHitsEvent(PostHitsEvent event) {
        log.info("onPostHitsEvent event:{}", event);
        postService.increaseHits(event.getPostId(), 1);
    }
}
