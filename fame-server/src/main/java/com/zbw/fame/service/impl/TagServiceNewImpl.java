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
import com.zbw.fame.service.TagServiceNew;
import com.zbw.fame.util.FameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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
public class TagServiceNewImpl extends ServiceImpl<TagMapper, Tag> implements TagServiceNew {

    private final ArticleTagService articleTagService;

    @Override
    public void delete(Integer id) {
        if (!removeById(id)) {
            throw new TipException("删除标签失败");
        }
        LambdaQueryWrapper<ArticleTag> wrapper = Wrappers.<ArticleTag>lambdaQuery()
                .eq(ArticleTag::getTagId, id);
        articleTagService.remove(wrapper);
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
        Set<Integer> tagIds = tags
                .stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        if (CollectionUtils.isEmpty(tagIds)) {
            return new ArrayList<>();
        }


        Map<Integer, List<Article>> articleMap = articleTagService.listArticleByTagIds(tagIds, isFront);
        Map<Integer, Tag> tagMap = tags
                .stream()
                .collect(Collectors.toMap(BaseEntity::getId, tag -> tag));

        List<TagInfoDto> tagInfos = new ArrayList<>();
        articleMap.forEach((tagId, articleList) -> {
            Tag tag = tagMap.get(tagId);
            TagInfoDto dto = new TagInfoDto();
            dto.setId(tag.getId());
            dto.setName(tag.getName());

            List<ArticleInfoDto> articleInfoDtos = articleList.stream()
                    .map(ArticleInfoDto::new)
                    .collect(Collectors.toList());
            dto.setArticleInfos(articleInfoDtos);
            tagInfos.add(dto);
        });


        return tagInfos;
    }
}
