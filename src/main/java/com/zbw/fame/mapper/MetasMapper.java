package com.zbw.fame.mapper;

import com.zbw.fame.dto.MetaDto;
import com.zbw.fame.model.Metas;
import com.zbw.fame.util.MyMapper;
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
}
