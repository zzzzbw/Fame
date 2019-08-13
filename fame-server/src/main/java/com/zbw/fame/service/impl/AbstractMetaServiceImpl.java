package com.zbw.fame.service.impl;

import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.model.domain.Meta;
import com.zbw.fame.model.domain.Middle;
import com.zbw.fame.model.domain.Post;
import com.zbw.fame.model.dto.PostInfo;
import com.zbw.fame.model.dto.MetaInfo;
import com.zbw.fame.model.enums.ArticleStatus;
import com.zbw.fame.repository.MetaRepository;
import com.zbw.fame.repository.MiddleRepository;
import com.zbw.fame.repository.PostRepository;
import com.zbw.fame.service.MetaService;
import com.zbw.fame.service.MiddleService;
import com.zbw.fame.util.FameUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public abstract class AbstractMetaServiceImpl<META extends Meta> implements MetaService<META> {

    protected final MiddleRepository middleRepository;

    protected final MetaRepository<META> metaRepository;

    protected final PostRepository postRepository;

    protected final MiddleService middleService;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Integer delete(String name) {
        META meta = metaRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException(FameUtil.getGenericClass(getClass())));
        metaRepository.delete(meta);
        return meta.getId();
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public META update(Integer id, String name) {
        if (StringUtils.isEmpty(name)) {
            throw new TipException("属性名不能为空");
        }

        META meta = metaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(FameUtil.getGenericClass(getClass())));
        meta.setName(name);
        return metaRepository.save(meta);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public boolean saveOrRemoveMetas(String nameStr, Integer articleId) {
        if (null == articleId) {
            throw new TipException("关联文章id不能为空");
        }

        removeMetas(nameStr, articleId);
        saveMetas(nameStr, articleId);
        return true;
    }


    /**
     * 添加names新加的属性到数据库
     *
     * @param nameStr   属性字符串
     * @param articleId 文章id
     */
    private void saveMetas(String nameStr, Integer articleId) {
        List<META> metas = findMetaByArticleId(articleId);
        Set<String> metaSet = new HashSet<>();
        for (META meta : metas) {
            metaSet.add(meta.getName());
        }
        String[] nameArr = nameStr.split(",");
        for (String name : nameArr) {
            if (StringUtils.isEmpty(name)) {
                continue;
            }
            if (!metaSet.contains(name)) {
                Meta meta = metaRepository.findByName(name)
                        .orElseGet(() -> save(name));
                middleRepository.save(new Middle(articleId, meta.getId()));
            }
        }
    }

    /**
     * 从数据库中删除names属性中没有的
     *
     * @param nameStr   属性字符串
     * @param articleId 文章id
     */
    private void removeMetas(String nameStr, Integer articleId) {
        String[] nameArr = nameStr.split(",");
        Set<String> nameSet = new HashSet<>(Arrays.asList(nameArr));
        List<META> metas = findMetaByArticleId(articleId);
        for (META meta : metas) {
            if (!nameSet.contains(meta.getName())) {
                middleRepository.deleteByArticleIdAndMetaId(articleId, meta.getId());
            }
        }
    }

    /**
     * 通过articleId获取关联的Meta
     *
     * @param articleId 文章id
     * @return List<Meta>
     */
    private List<META> findMetaByArticleId(Integer articleId) {
        Set<Integer> metaIds = middleService.getMetaIdsByArticleId(articleId);
        return metaRepository.findAllById(metaIds);
    }

    @Override
    public List<MetaInfo> getFrontMetaInfos() {
        List<META> metas = metaRepository.findAll();
        List<Post> posts = postRepository.findAllByStatus(ArticleStatus.PUBLISH, FameUtil.sortDescById());
        return getMetaInfos(metas, posts);
    }

    @Override
    public List<MetaInfo> getAdminMetaInfos() {
        List<META> metas = metaRepository.findAll();
        List<Post> posts = postRepository.findAllByStatusNot(ArticleStatus.DELETE, FameUtil.sortDescById());
        return getMetaInfos(metas, posts);
    }

    /**
     * 获取MetaInfo列表
     *
     * @param metas 要关联的属性列表
     * @param posts 要关联的文章列表
     * @return List<MetaInfo>
     */
    private List<MetaInfo> getMetaInfos(List<? extends Meta> metas, List<Post> posts) {
        // 转换成 key-domain Map
        Map<Integer, Post> postMap = posts.stream().collect(Collectors.toMap(Post::getId, post -> post));
        // 属性对应关联列表
        Map<Integer, List<Middle>> metaIdMiddleListMap = middleService.getMetaIdMiddleListMap();

        return metas.stream().map(meta -> {
            MetaInfo metaInfo = new MetaInfo();
            metaInfo.setId(meta.getId());
            metaInfo.setName(meta.getName());

            List<Middle> middleList = metaIdMiddleListMap.computeIfAbsent(meta.getId(), article -> new ArrayList<>());

            List<PostInfo> articleInfoList = middleList.stream()
                    .map(middle -> postMap.get(middle.getArticleId()))
                    .filter(Objects::nonNull)
                    .map(PostInfo::new)
                    .collect(Collectors.toList());
            metaInfo.setPostInfos(articleInfoList);

            return metaInfo;
        }).collect(Collectors.toList());
    }
}
