package com.zbw.fame.mapper;

import com.zbw.fame.model.dto.MetaDto;
import com.zbw.fame.model.domain.Metas;
import com.zbw.fame.util.MyMapper;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Metas Mapper
 *
 * @author zbw
 * @since 2017/8/28 23:40
 */
public interface MetasMapper extends MyMapper<Metas> {

    /**
     * 根据文章id获取该文章下的属性
     *
     * @param articleId 文章id
     * @param type      属性类型
     * @return List<Metas>
     */
    @Select("SELECT * FROM fame.metas WHERE type = #{type} AND id IN (SELECT m_id FROM fame.middles WHERE a_id = #{articleId})")
    List<Metas> selectByArticles(@Param("articleId") Integer articleId, @Param("type") String type);

    /**
     * 获取属性以及属性下的文章
     *
     * @param type 属性类型
     * @return List<MetaDto>
     */
    @Select("select * from fame.metas meta where meta.type = #{type}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "type", property = "type"),
            @Result(column = "id", property = "count",
                    one = @One(select = "com.zbw.fame.mapper.ArticlesMapper.selectCountByMetas"))
    })
    List<MetaDto> selectMetasDto(@Param("type") String type);

    /**
     * 获取属性以及属性下的已发布文章
     *
     * @param type 属性类型
     * @return List<MetaDto>
     */
    @Select("select * from fame.metas meta where meta.type = #{type}")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "name", property = "name"),
            @Result(column = "type", property = "type"),
            @Result(column = "id", property = "count",
                    one = @One(select = "com.zbw.fame.mapper.ArticlesMapper.selectPublishCountByMetas")),
            @Result(column = "id", property = "articles",
                    many = @Many(select = "com.zbw.fame.mapper.ArticlesMapper.selectPublishByMetas"))
    })
    List<MetaDto> selectMetasDtoPublish(@Param("type") String type);

    /**
     * 获取所有属性
     *
     * @param type 属性类型
     * @return List<MetaDto>
     */
    @Select("SELECT name, type ,count(*) as articleCount FROM fame.metas WHERE type = #{type} GROUP BY name")
    List<MetaDto> selectMetasDistinct(@Param("type") String type);

}
