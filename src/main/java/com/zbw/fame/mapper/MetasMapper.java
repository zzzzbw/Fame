package com.zbw.fame.mapper;

import com.zbw.fame.dto.MetaDto;
import com.zbw.fame.model.Metas;
import com.zbw.fame.util.MyMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Metas Mapper
 *
 * @auther zbw
 * @create 2017/8/28 23:40
 */
public interface MetasMapper extends MyMapper<Metas> {


    @Select("SELECT * FROM fame.metas WHERE type = #{type} AND id IN (SELECT m_id FROM fame.middles WHERE a_id = #{articleId})")
    List<Metas> selectByArticles(@Param("articleId") Integer articleId, @Param("type") String type);

    @Select("select * from articles ar inner join middles m on ar.id=m.a_id inner join metas me on me.id=m.m_id")
    List<MetaDto> selectMetasDto();

    @Select("SELECT name, type ,count(*) as articleCount FROM fame.metas WHERE type = #{type} GROUP BY name")
    List<MetaDto> selectMetasDistinct(String type);

}
