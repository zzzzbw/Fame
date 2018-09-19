package com.zbw.fame.mapper;

import com.zbw.fame.dto.ArticleInfoDto;
import com.zbw.fame.model.Articles;
import com.zbw.fame.util.MyMapper;
import com.zbw.fame.util.Types;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Articles Mapper
 *
 * @author zbw
 * @since 2017/7/8 10:39
 */
public interface ArticlesMapper extends MyMapper<Articles> {

    /**
     * 根据metaId获取关联的文章
     *
     * @param metaId 属性id
     * @return List<Articles>
     */
    @Select("SELECT * FROM fame.articles WHERE id IN (SELECT a_id FROM fame.middles WHERE m_id = #{metaId}) " +
            "AND fame.articles.type = '" + Types.POST + "' order by created desc")
    List<Articles> selectByMetas(@Param("metaId") Integer metaId);

    /**
     * 根据metaId获取关联文章的数量
     *
     * @param metaId 属性id
     * @return Integer
     */
    @Select("SELECT count(*) FROM fame.articles WHERE id IN (SELECT a_id FROM fame.middles WHERE m_id = #{metaId}) " +
            "AND fame.articles.TYPE = '" + Types.POST + "' AND fame.articles.type = '" + Types.POST + "'")
    Integer selectCountByMetas(@Param("metaId") Integer metaId);

    /**
     * 根据metaId获取关联的已发布文章
     *
     * @param metaId 属性id
     * @return List<Articles>
     */
    @Select("SELECT * FROM fame.articles WHERE id " +
            "IN (SELECT a_id FROM fame.middles WHERE m_id = #{metaId}) " +
            "AND fame.articles.status = '" + Types.PUBLISH + "' AND fame.articles.type = '" + Types.POST + "' order by created desc")
    List<ArticleInfoDto> selectPublishByMetas(@Param("metaId") Integer metaId);

    /**
     * 根据metaId获取关联已发布文章的数量
     *
     * @param metaId 属性id
     * @return Integer
     */
    @Select("SELECT count(*) FROM fame.articles WHERE id " +
            "IN (SELECT a_id FROM fame.middles WHERE m_id = #{metaId}) " +
            "AND fame.articles.status = '" + Types.PUBLISH + "' AND fame.articles.type = '" + Types.POST + "'")
    Integer selectPublishCountByMetas(@Param("metaId") Integer metaId);

}
