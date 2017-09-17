package com.zbw.fame.mapper;

import com.zbw.fame.dto.MetaDto;
import com.zbw.fame.model.Metas;
import com.zbw.fame.util.MyMapper;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Metas Mapper
 *
 * @auther zbw
 * @create 2017/8/28 23:40
 */
public interface MetasMapper extends MyMapper<Metas> {

    @Select("SELECT name, type ,count(*) as articleCount FROM fame.metas WHERE type = #{type} GROUP BY name")
    List<MetaDto> selectMetasDistinct(String type);


    @Select("SELECT * FROM fame.metas")
    @Results({
            @Result(id = true, column = "id", property = "id"),
            @Result(column = "name"),
            @Result(column = "article_id", property = "articles",
                    many = @Many(
                            select = "com.zbw.fame.mapper.ArticlesMapper.selectByPrimaryKey"
                    ))
    })
    List<MetaDto> selectMetasWithArticles(String type);
}
