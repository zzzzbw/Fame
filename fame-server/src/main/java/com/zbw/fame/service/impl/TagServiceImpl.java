package com.zbw.fame.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.mapper.TagMapper;
import com.zbw.fame.model.dto.ArticleInfoDto;
import com.zbw.fame.model.dto.TagInfoDto;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.entity.ArticleTag;
import com.zbw.fame.model.entity.BaseEntity;
import com.zbw.fame.model.entity.Tag;
import com.zbw.fame.model.param.SaveTagParam;
import com.zbw.fame.service.ArticleTagService;
import com.zbw.fame.service.TagService;
import com.zbw.fame.util.FameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author by zzzzbw
 * @since 2021/03/15 11:30
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {

    private final ArticleTagService articleTagService;

    @Override
    public void delete(Integer id) {
        if (!removeById(id)) {
            throw new TipException("删除标签失败");
        }
        articleTagService.deleteByTagId(id);
    }

    @Override
    public Tag createOrUpdate(SaveTagParam param) {
        Tag tag = FameUtils.convertTo(param, Tag.class);
        saveOrUpdate(tag);
        return tag;
    }

    @Override
    public List<TagInfoDto> listTagInfo(boolean isFront) {
        List<Tag> tags = list();
        if (CollectionUtils.isEmpty(tags)) {
            return Collections.emptyList();
        }
        Set<Integer> tagIds = tags
                .stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());
        Map<Integer, List<Article>> articleMap = articleTagService.listArticleByTagIds(tagIds, isFront);

        return tags.stream()
                .map(tag -> {
                    TagInfoDto dto = new TagInfoDto();
                    dto.setId(tag.getId());
                    dto.setName(tag.getName());

                    List<ArticleInfoDto> articleInfoDtos = articleMap.getOrDefault(tag.getId(), Collections.emptyList())
                            .stream()
                            .map(ArticleInfoDto::new)
                            .collect(Collectors.toList());
                    dto.setArticleInfos(articleInfoDtos);
                    return dto;
                }).collect(Collectors.toList());
    }
}
