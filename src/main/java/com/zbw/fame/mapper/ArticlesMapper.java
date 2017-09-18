package com.zbw.fame.mapper;

import com.zbw.fame.model.Articles;
import com.zbw.fame.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Articles Mapper
 *
 * @auther zbw
 * @create 2017/7/8 10:39
 */
public interface ArticlesMapper extends MyMapper<Articles>{

    @Select("SELECT * FROM fame.articles WHERE id IN (SELECT m_id FROM fame.middles WHERE a_id = #{articleId})")
    List<Articles> selectByMetas(@Param("metaId") Integer metaId, @Param("type") String type);

}
