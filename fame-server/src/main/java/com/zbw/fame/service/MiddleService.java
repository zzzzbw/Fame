package com.zbw.fame.service;

import com.zbw.fame.model.domain.Middle;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangbowen
 * @since 2019/7/19 15:51
 */
public interface MiddleService {

    /**
     * 获取 属性Id-Middle列表 Map
     *
     * @return Map
     */
    Map<Integer, List<Middle>> getMetaIdMiddleListMap();

    /**
     * 根据文章id获得属性id列表
     *
     * @param articleId 文章id
     * @return 属性id列表
     */
    Set<Integer> getMetaIdsByArticleId(Integer articleId);

    /**
     * 根据属性id获得文章id列表
     *
     * @param metaId 属性id
     * @return 文章id列表
     */
    Set<Integer> getArticleIdsByMetaId(Integer metaId);
}
