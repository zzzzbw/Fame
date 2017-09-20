package com.zbw.fame.mapper;

import com.zbw.fame.dto.MetaDto;
import com.zbw.fame.model.Metas;
import com.zbw.fame.util.MyMapper;
import org.apache.ibatis.annotations.*;

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

    @Select("select * from fame.metas meta where meta.type = #{type}")
    @Results({
            @Result(id = true, column = "meta.id", property = "id"),
            @Result(column = "meta.name", property = "name"),
            @Result(column = "meta.type", property = "type"),
            @Result(column = "id", property = "count",
                    one = @One(select = "com.zbw.fame.mapper.ArticlesMapper.selectCountByMetas")),
            @Result(column = "id", property = "articles",
                    many = @Many(select = "com.zbw.fame.mapper.ArticlesMapper.selectByMetas"))
    })
    List<MetaDto> selectMetasDto(@Param("type") String type);

    @Select("select * from fame.metas meta where meta.type = #{type}")
    @Results({
            @Result(id = true, column = "meta.id", property = "id"),
            @Result(column = "meta.name", property = "name"),
            @Result(column = "meta.type", property = "type"),
            @Result(column = "id", property = "count",
                    one = @One(select = "com.zbw.fame.mapper.ArticlesMapper.selectPublishCountByMetas")),
            @Result(column = "id", property = "articles",
                    many = @Many(select = "com.zbw.fame.mapper.ArticlesMapper.selectPublishByMetas"))
    })
    List<MetaDto> selectMetasDtoPublish(@Param("type") String type);

    @Select("SELECT name, type ,count(*) as articleCount FROM fame.metas WHERE type = #{type} GROUP BY name")
    List<MetaDto> selectMetasDistinct(@Param("type") String type);

}
