package com.zbw.fame.mapper;

import com.zbw.fame.model.Articles;
import com.zbw.fame.util.MyMapper;
import com.zbw.fame.util.Types;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Articles Mapper
 *
 * @auther zbw
 * @create 2017/7/8 10:39
 */
public interface ArticlesMapper extends MyMapper<Articles> {

    @Select("SELECT * FROM fame.articles WHERE id IN (SELECT a_id FROM fame.middles WHERE m_id = #{metaId})")
    List<Articles> selectByMetas(@Param("metaId") Integer metaId);

    @Select("SELECT count(*) FROM fame.articles WHERE id IN (SELECT a_id FROM fame.middles WHERE m_id = #{metaId})")
    Integer selectCountByMetas(@Param("metaId") Integer metaId);

    @Select("SELECT * FROM fame.articles WHERE id IN (SELECT a_id FROM fame.middles WHERE m_id = #{metaId}) AND fame.articles.status = '" + Types.PUBLISH + "'")
    List<Articles> selectPublishByMetas(@Param("metaId") Integer metaId);

    @Select("SELECT count(*) FROM fame.articles WHERE id IN (SELECT a_id FROM fame.middles WHERE m_id = #{metaId}) AND fame.articles.status = '" + Types.PUBLISH + "'")
    Integer selectPublishCountByMetas(@Param("metaId") Integer metaId);

}
