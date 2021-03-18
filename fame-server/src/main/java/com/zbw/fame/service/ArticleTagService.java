package com.zbw.fame.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.entity.ArticleTag;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author by zzzzbw
 * @since 2021/03/15 16:53
 */
public interface ArticleTagService extends IService<ArticleTag> {

    /**
     * 获取标签下的文章列表
     * key: tagId
     * value: 文章列表
     *
     * @param tagIds
     * @param isFront
     * @return
     */
    Map<Integer, List<Article>> listArticleByTagIds(Collection<Integer> tagIds, boolean isFront);
}
