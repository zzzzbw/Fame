package com.zbw.fame.service.impl;

import com.zbw.fame.mapper.MetasMapper;
import com.zbw.fame.model.Metas;
import com.zbw.fame.service.MetasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 属性 Service 实现类
 *
 * @auther zbw
 * @create 2017/8/28 23:33
 */
@Service("metasService")
public class MetasServiceImpl implements MetasService {

    @Autowired
    private MetasMapper metasMapper;

    @Override
    public List<Metas> getMetas(String type) {
        Metas meta = new Metas();
        meta.setType(type);
        return metasMapper.select(meta);
    }

    @Override
    public boolean deleteMetas(String name, String type) {
        Metas meta = new Metas();
        meta.setName(name);
        meta.setType(type);
        return metasMapper.delete(meta) > 0;
    }

    @Override
    public boolean saveMetas(String name, String type, Integer aId) {
        Metas meta = new Metas();
        meta.setType(type);
        meta.setName(name);
        meta.setaId(aId);
        return metasMapper.insert(meta) > 0;
    }
}
