package com.zbw.fame.service;

import com.zbw.fame.dto.MetaDto;

import java.util.List;

/**
 * 属性 Service 接口
 *
 * @auther zbw
 * @create 2017/8/28 23:32
 */
public interface MetasService {
    List<MetaDto> getMetaDtos(String type);

    List<MetaDto> getMetaDto(String type);

    boolean deleteMeta(String name, String type);

    boolean saveMeta(String name, String type);

    boolean updateMeta(Integer id, String name, String type);

    boolean saveOrRemoveMetas(String names, String type, Integer articleId);

}
