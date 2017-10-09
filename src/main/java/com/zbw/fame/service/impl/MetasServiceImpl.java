package com.zbw.fame.service.impl;

import com.zbw.fame.dto.MetaDto;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.mapper.ArticlesMapper;
import com.zbw.fame.mapper.MetasMapper;
import com.zbw.fame.mapper.MiddlesMapper;
import com.zbw.fame.model.Articles;
import com.zbw.fame.model.Metas;
import com.zbw.fame.model.Middles;
import com.zbw.fame.service.MetasService;
import com.zbw.fame.util.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 属性 Service 实现类
 *
 * @auther zbw
 * @create 2017/8/28 23:33
 */
@Service("metasService")
public class MetasServiceImpl implements MetasService {

    @Autowired
    private MiddlesMapper middlesMapper;

    @Autowired
    private MetasMapper metasMapper;

    @Autowired
    private ArticlesMapper articlesMapper;

    @Override
    public List<MetaDto> getMetaDtos(String type) {
        type = verifyType(type);
        return metasMapper.selectMetasDtoPublish(type);
    }


    public List<MetaDto> getMetaDto(String type) {
        type = verifyType(type);
        return metasMapper.selectMetasDto(type);
    }

    @Override
    public boolean deleteMeta(String name, String type) {
        type = verifyType(type);
        Metas meta = metasMapper.selectOne(new Metas(name, type));
        if (null == meta) {
            throw new TipException("没有该名称的属性");
        }
        List<Middles> middles = middlesMapper.select(new Middles(null, meta.getId()));
        for (Middles middle : middles) {
            Articles articles = articlesMapper.selectByPrimaryKey(middle.getaId());
            if (null != articles) {
                if (type.equals(Types.CATEGORY)) {
                    articles.setCategory("");
                }
                if (type.equals(Types.TAG)) {
                    articles.setTags(this.resetMeta(name, articles.getTags()));
                }
                articlesMapper.updateByPrimaryKey(articles);
            }
        }

        middlesMapper.delete(new Middles(null, meta.getId()));
        metasMapper.delete(meta);
        return true;
    }

    @Override
    public boolean saveMeta(String name, String type) {
        type = verifyType(type);
        Metas metas = new Metas();
        metas.setType(type);
        metas.setName(name);
        if (metasMapper.select(metas).size() > 0) {
            throw new TipException("该属性已经存在");
        }
        return metasMapper.insert(metas) > 0;
    }

    @Override
    public boolean updateMeta(Integer id, String name, String type) {
        type = verifyType(type);
        Metas meta = metasMapper.selectByPrimaryKey(id);
        if (null == meta) {
            throw new TipException("没有该属性");
        }
        List<Articles> articles = articlesMapper.selectByMetas(id);
        for (Articles article : articles) {
            String metas;
            if (type.equals(Types.CATEGORY)) {
                metas = article.getCategory();
                String newMetas = metas.replace(meta.getName(), name);
                if (!newMetas.equals(metas)) {
                    article.setCategory(newMetas);
                    articlesMapper.updateByPrimaryKey(article);
                }
            }
            if (type.equals(Types.TAG)) {
                metas = article.getTags();
                String newMetas = metas.replace(meta.getName(), name);
                if (!newMetas.equals(metas)) {
                    article.setTags(newMetas);
                    articlesMapper.updateByPrimaryKey(article);
                }
            }
        }
        meta.setName(name);
        return metasMapper.updateByPrimaryKey(meta) > 0;
    }

    @Override
    public boolean saveOrRemoveMetas(String names, String type, Integer articleId) {
        type = verifyType(type);
        if (null == articleId) {
            throw new TipException("关联文章id不能为空");
        }

        if (StringUtils.isEmpty(names)) {
            middlesMapper.delete(new Middles(articleId, null));
            return true;
        }

        removeMetas(names, type, articleId);
        saveMetas(names, type, articleId);
        return true;
    }


    /**
     * 添加names新加的属性到数据库
     *
     * @param names
     * @param type
     * @param articleId
     */
    private void saveMetas(String names, String type, Integer articleId) {
        List<Metas> metas = metasMapper.selectByArticles(articleId, type);
        Set<String> metaSet = new HashSet<>();
        for (Metas meta : metas) {
            metaSet.add(meta.getName());
        }
        String[] nameArr = names.split(",");
        for (String name : nameArr) {
            if (!metaSet.contains(name)) {
                Metas newMeta = new Metas(name, type);
                Metas meta = metasMapper.selectOne(newMeta);
                if (null == meta) {
                    metasMapper.insert(newMeta);
                } else {
                    newMeta = meta;
                }
                middlesMapper.insert(new Middles(articleId, newMeta.getId()));
            }
        }
    }

    /**
     * 从数据库中删除names属性中没有的
     *
     * @param names
     * @param type
     * @param articleId
     */
    private void removeMetas(String names, String type, Integer articleId) {
        String[] nameArr = names.split(",");
        Set<String> nameSet = new HashSet<>(Arrays.asList(nameArr));
        List<Metas> metas = metasMapper.selectByArticles(articleId, type);
        for (Metas meta : metas) {
            if (!nameSet.contains(meta.getName())) {
                middlesMapper.delete(new Middles(articleId, meta.getId()));
            }
        }

    }


    /**
     * 验证Type是否为定义的
     *
     * @return
     */
    private String verifyType(String type) {
        switch (type) {
            case Types.CATEGORY:
                return type;
            case Types.TAG:
                return type;
            default:
                throw new TipException("传输的属性类型不合法");
        }
    }

    /**
     * 从属性字符串中去除一个属性
     *
     * @param name
     * @param metas
     * @return
     */
    private String resetMeta(String name, String metas) {
        String[] metaArr = metas.split(",");
        StringBuffer sb = new StringBuffer();
        for (String m : metaArr) {
            if (!name.equals(m)) {
                sb.append(",").append(m);
            }
        }
        if (sb.length() > 0) {
            return sb.substring(1);
        }
        return "";
    }

}
