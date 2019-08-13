package com.zbw.fame.repository;

import com.zbw.fame.model.domain.Middle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author zhangbowen
 * @since 2019/6/24 10:14
 */
public interface MiddleRepository extends JpaRepository<Middle, Integer> {

    /**
     * 获取文章下关联的属性
     *
     * @param articleId 文章id
     * @return 关联
     */
    List<Middle> findAllByArticleId(Integer articleId);

    /**
     * 获取属性下关联的文章
     *
     * @param metaId 属性id
     * @return 关联
     */
    List<Middle> findAllByMetaId(Integer metaId);

    /**
     * 删除文章下关联的属性
     *
     * @param articleId 文章id
     * @return 删除数量
     */
    int deleteAllByArticleId(Integer articleId);

    /**
     * 删除属性下关联的文章
     *
     * @param metaId 属性id
     * @return 删除数量
     */
    int deleteAllByMetaId(Integer metaId);

    /**
     * 根据文章id和属性id删除关联
     *
     * @param articleId 文章id
     * @param metaId 属性id
     * @return 删除数量
     */
    int deleteByArticleIdAndMetaId(Integer articleId, Integer metaId);
}
