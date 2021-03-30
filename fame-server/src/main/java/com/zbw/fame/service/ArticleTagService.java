package com.zbw.fame.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.entity.ArticleTag;
import com.zbw.fame.model.entity.Tag;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author by zzzzbw
 * @since 2021/03/15 16:53
 */
public interface ArticleTagService extends IService<ArticleTag> {

    /**
     * 根据文章Id获取=标签
     *
     * @param articleId
     * @return
     */
    List<Tag> listTagByArticleId(Integer articleId);

    /**
     * 根据标签id删除关联
     *
     * @param tagId
     */
    void deleteByTagId(Integer tagId);

    /**
     * 根据文章id删除关联
     *
     * @param articleId
     */
    void deleteByArticleId(Integer articleId);


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

    /**
     * 获取文章下的标签列表
     *
     * @param articleIds
     * @return
     */
    Map<Integer, List<Tag>> listTagByArticleIds(Collection<Integer> articleIds);

    /**
     * 保存或更新文章标签关联
     *
     * @param articleId
     * @param tagIds
     */
    void createOrUpdate(Integer articleId, Set<Integer> tagIds);
}
