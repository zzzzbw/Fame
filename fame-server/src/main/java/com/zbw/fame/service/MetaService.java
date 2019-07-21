package com.zbw.fame.service;

import com.zbw.fame.model.dto.MetaInfo;

import java.util.List;

/**
 * 属性 Service 接口
 *
 * @author zbw
 * @since 2017/8/28 23:32
 */
public interface MetaService<META> {
    /**
     * 删除属性(同时删除关联文章的属性)
     *
     * @param name 属性名
     * @return Integer 删除的属性id
     */
    Integer delete(String name);

    /**
     * 保存属性
     *
     * @param name 属性名
     * @return 保存后的属性实体
     */
    META save(String name);

    /**
     * 更新属性(同时更新关联文章的属性)
     *
     * @param id   属性id
     * @param name 属性名
     * @return 存后的属性实体
     */
    META update(Integer id, String name);

    /**
     * 添加或者删除属性(同时添加或者删除关联文章的属性)
     *
     * @param names     属性名
     * @param articleId 关联文章id
     * @return boolean
     */
    boolean saveOrRemoveMetas(String names, Integer articleId);

    /**
     * 获取前端属性信息
     *
     * @return List<MetaInfo>
     */
    List<MetaInfo> getFrontMetaInfos();

    /**
     * 获取后台属性信息
     *
     * @return List<MetaInfo>
     */
    List<MetaInfo> getAdminMetaInfos();

}
