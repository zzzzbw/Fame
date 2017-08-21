package com.zbw.fame.service;

import com.zbw.fame.model.Metas;

import java.util.List;

/**
 * 属性 Service 接口
 *
 * @auther zbw
 * @create 2017/8/28 23:32
 */
public interface MetasService {
    List<Metas> getMetas(String type);

    boolean deleteMetas(String name, String type);

    boolean saveMetas(String name, String type, Integer aId);
}
