package com.zbw.fame.mapper;

import com.zbw.fame.dto.Page;
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

    @Select("SELECT * FROM fame.articles WHERE id IN (SELECT a_id FROM fame.middles WHERE m_id = #{metaId}) " +
            "AND fame.articles.type = '" + Types.POST + "'")
    List<Articles> selectByMetas(@Param("metaId") Integer metaId);

    @Select("SELECT count(*) FROM fame.articles WHERE id IN (SELECT a_id FROM fame.middles WHERE m_id = #{metaId}) " +
            "AND fame.articles.TYPE = '" + Types.POST + "' AND fame.articles.type = '" + Types.POST + "'")
    Integer selectCountByMetas(@Param("metaId") Integer metaId);

    @Select("SELECT * FROM fame.articles WHERE id " +
            "IN (SELECT a_id FROM fame.middles WHERE m_id = #{metaId}) " +
            "AND fame.articles.status = '" + Types.PUBLISH + "' AND fame.articles.type = '" + Types.POST + "'")
    List<Articles> selectPublishByMetas(@Param("metaId") Integer metaId);

    @Select("SELECT count(*) FROM fame.articles WHERE id " +
            "IN (SELECT a_id FROM fame.middles WHERE m_id = #{metaId}) " +
            "AND fame.articles.status = '" + Types.PUBLISH + "' AND fame.articles.type = '" + Types.POST + "'")
    Integer selectPublishCountByMetas(@Param("metaId") Integer metaId);

    @Select("SELECT * FROM fame.articles WHERE fame.articles.type = '" + Types.POST + "'")
    List<Page> selectPages();

    @Select("SELECT * FROM fame.articles WHERE fame.articles.title = #{title} AND fame.articles.type = '" + Types.POST + "'")
    Page getPage(@Param("title") String title);

}
