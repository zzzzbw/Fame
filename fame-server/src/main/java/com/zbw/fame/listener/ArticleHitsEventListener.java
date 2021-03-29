package com.zbw.fame.listener;

import com.zbw.fame.listener.event.ArticleHitsEvent;
import com.zbw.fame.service.ArticleService;
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
public class ArticleHitsEventListener {

    private final ArticleService articleService;

    @Async
    @EventListener
    public void onArticleHitsEvent(ArticleHitsEvent event) {
        log.info("onArticleHitsEvent event:{}", event);
        articleService.increaseHits(event.getArticleId(), 1);
    }
}
