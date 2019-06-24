package com.zbw.fame.service.impl;

import com.zbw.fame.exception.TipException;
import com.zbw.fame.model.domain.Article;
import com.zbw.fame.model.domain.Meta;
import com.zbw.fame.model.domain.Middle;
import com.zbw.fame.model.dto.ArticleInfo;
import com.zbw.fame.model.dto.MetaInfo;
import com.zbw.fame.repository.ArticleRepository;
import com.zbw.fame.repository.MetaRepository;
import com.zbw.fame.repository.MiddleRepository;
import com.zbw.fame.service.MetaService;
import com.zbw.fame.util.Types;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 属性 Service 实现类
 *
 * @author zbw
 * @since 2017/8/28 23:33
 */
@Service("metasService")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MetaServiceImpl implements MetaService {

    private final MiddleRepository middleRepository;

    private final MetaRepository metaRepository;

    private final ArticleRepository articleRepository;


    @Override
    public List<MetaInfo> getPublishMetaInfos(String type) {
        type = verifyType(type);

        List<Meta> metas = metaRepository.findAllByType(type);

        List<Article> articles = articleRepository.findAllByStatusNotAndType(Types.DELETE, Types.POST);
        Map<Integer, Article> articleMap = articles.stream().collect(Collectors.toMap(Article::getId, article -> article));

        return getMetaInfo(metas, articleMap);
    }

    @Override
    public List<MetaInfo> getMetaInfos(String type) {
        type = verifyType(type);

        List<Meta> metas = metaRepository.findAllByType(type);

        List<Article> articles = articleRepository.findAllByStatusAndType(Types.PUBLISH, Types.POST);
        Map<Integer, Article> articleMap = articles.stream().collect(Collectors.toMap(Article::getId, article -> article));

        return getMetaInfo(metas, articleMap);
    }

    /**
     * 获取MetaInfo列表
     *
     * @param metas      要关联的属性列表
     * @param articleMap 要关联的文章 主键-实体类 Map
     * @return List<MetaInfo>
     */
    private List<MetaInfo> getMetaInfo(List<Meta> metas, Map<Integer, Article> articleMap) {
        // 属性对应关联列表
        List<Middle> middles = middleRepository.findAll();
        Map<Integer, List<Middle>> metaIdMiddleListMap = middles.stream().collect(Collectors.groupingBy(Middle::getMId));

        return metas.stream().map(meta -> {
            MetaInfo metaInfo = new MetaInfo();
            metaInfo.setId(meta.getId());
            metaInfo.setType(meta.getType());
            metaInfo.setName(meta.getName());

            List<Middle> middleList = metaIdMiddleListMap.computeIfAbsent(meta.getId(), article -> new ArrayList<>());

            List<ArticleInfo> articleInfoList = middleList.stream()
                    .map(middle -> articleMap.get(middle.getAId()))
                    .map(this::convertArticleToArticleInfo)
                    .collect(Collectors.toList());
            metaInfo.setArticles(articleInfoList);
            metaInfo.setArticleCount(articleInfoList.size());

            return metaInfo;
        }).collect(Collectors.toList());
    }


    /**
     * 转换Article为ArticleInfo
     *
     * @param article Article
     * @return ArticleInfo
     */
    private ArticleInfo convertArticleToArticleInfo(Article article) {
        ArticleInfo info = new ArticleInfo();
        info.setId(article.getId());
        info.setTitle(article.getTitle());
        return info;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean deleteMeta(String name, String type) {
        final String verifyType = verifyType(type);

        Meta meta = metaRepository.findByNameAndType(name, type)
                .orElseThrow(() -> new TipException("没有该名称的属性"));

        List<Middle> middles = middleRepository.findAllByMId(meta.getId());
        for (Middle middle : middles) {
            articleRepository.findById(middle.getAId()).ifPresent(article -> {
                if (verifyType.equals(Types.CATEGORY)) {
                    article.setCategory("");
                }
                if (verifyType.equals(Types.TAG)) {
                    article.setTags(this.resetMeta(name, article.getTags()));
                }
                articleRepository.save(article);
            });
        }

        middleRepository.deleteAllByMId(meta.getId());
        metaRepository.delete(meta);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean saveMeta(String name, String type) {
        if (StringUtils.isEmpty(name)) {
            throw new TipException("属性名不能为空");
        }
        type = verifyType(type);
        Meta meta = new Meta();
        meta.setType(type);
        meta.setName(name);

        metaRepository.findByNameAndType(name, type).ifPresent(result -> {
            throw new TipException("该属性已经存在");
        });

        return metaRepository.save(meta) != null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean updateMeta(Integer id, String name, String type) {
        if (StringUtils.isEmpty(name)) {
            throw new TipException("属性名不能为空");
        }
        type = verifyType(type);
        Meta meta = metaRepository.findById(id)
                .orElseThrow(() -> new TipException("没有该属性"));


        Set<Integer> articleIds = middleRepository.findAllByMId(id)
                .stream()
                .map(Middle::getAId)
                .collect(Collectors.toSet());
        List<Article> articles = articleRepository.findAllById(articleIds);
        for (Article article : articles) {
            String metaStr;
            if (type.equals(Types.CATEGORY)) {
                metaStr = article.getCategory();
                String newMetas = metaStr.replace(meta.getName(), name);
                if (!newMetas.equals(metaStr)) {
                    article.setCategory(newMetas);
                    articleRepository.save(article);
                }
            }
            if (type.equals(Types.TAG)) {
                metaStr = article.getTags();
                String newMetas = metaStr.replace(meta.getName(), name);
                if (!newMetas.equals(metaStr)) {
                    article.setTags(newMetas);
                    articleRepository.save(article);
                }
            }
        }
        meta.setName(name);
        return metaRepository.save(meta) != null;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean saveOrRemoveMetas(String names, String type, Integer articleId) {
        type = verifyType(type);
        if (null == articleId) {
            throw new TipException("关联文章id不能为空");
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
        List<Meta> metas = findMetaByArticleId(articleId, type);
        Set<String> metaSet = new HashSet<>();
        for (Meta meta : metas) {
            metaSet.add(meta.getName());
        }
        String[] nameArr = names.split(",");
        for (String name : nameArr) {
            if (StringUtils.isEmpty(name)) {
                continue;
            }
            if (!metaSet.contains(name)) {
                final Meta newMeta = new Meta(name, type);
                Meta meta = metaRepository.findByNameAndType(name, type)
                        .orElseGet(() -> metaRepository.save(newMeta));
                middleRepository.save(new Middle(articleId, meta.getId()));
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
        List<Meta> metas = findMetaByArticleId(articleId, type);
        for (Meta meta : metas) {
            if (!nameSet.contains(meta.getName())) {
                middleRepository.deleteByAIdAndMId(articleId, meta.getId());
            }
        }
    }

    /**
     * 通过articleId获取关联的Meta
     *
     * @param articleId 文章id
     * @param type      属性类型
     * @return List<Meta>
     */
    private List<Meta> findMetaByArticleId(Integer articleId, String type) {
        Set<Integer> metaIds = middleRepository.findAllByAId(articleId)
                .stream()
                .map(Middle::getMId)
                .collect(Collectors.toSet());

        return metaRepository.findAllById(metaIds)
                .stream()
                .filter(meta -> meta.getType().equalsIgnoreCase(type))
                .collect(Collectors.toList());
    }

    /**
     * 验证Type是否为定义的
     *
     * @return
     */
    private String verifyType(String type) {
        switch (type) {
            case Types.CATEGORY:
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
        StringBuilder sb = new StringBuilder();
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
