package com.zbw.fame.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.mapper.CategoryMapper;
import com.zbw.fame.model.dto.ArticleInfoDto;
import com.zbw.fame.model.dto.CategoryInfoDto;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.entity.BaseEntity;
import com.zbw.fame.model.entity.Category;
import com.zbw.fame.model.param.SaveCategoryParam;
import com.zbw.fame.service.ArticleCategoryService;
import com.zbw.fame.service.CategoryService;
import com.zbw.fame.util.FameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author by zzzzbw
 * @since 2021/03/19 10:26
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    private final ArticleCategoryService articleCategoryService;

    @Override
    public void delete(Integer id) {
        recursionDelete(id);
    }

    private void recursionDelete(Integer id) {
        Category category = getById(id);
        if (null == category) {
            return;
        }
        removeById(id);
        articleCategoryService.deleteByCategoryId(id);

        if (null != category.getParentId()) {
            recursionDelete(category.getParentId());
        }
    }

    @Override
    public Category createOrUpdate(SaveCategoryParam param) {
        Integer parentId = param.getParentId();
        if (null != parentId) {
            Optional.ofNullable(getById(parentId))
                    .orElseThrow(() -> new NotFoundException(Category.class));
        }

        Category category = FameUtils.convertTo(param, Category.class);
        saveOrUpdate(category);
        return category;
    }

    @Override
    public List<CategoryInfoDto> listCategoryInfo(boolean isFront) {
        List<Category> categories = list();
        if (CollectionUtils.isEmpty(categories)) {
            return Collections.emptyList();
        }

        Set<Integer> categoryIds = categories
                .stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());

        Map<Integer, List<Article>> articleMap = articleCategoryService.listArticleByCategoryIds(categoryIds, isFront);
        Map<Integer, List<Category>> childCategoryMap = categories.stream()
                .filter(category -> null != category.getParentId())
                .collect(Collectors.groupingBy(Category::getParentId));

        return categories.stream()
                .filter(category -> category.getParentId() == null)
                .map(category -> categoryToCategoryInfoDto(category, childCategoryMap, articleMap))
                .collect(Collectors.toList());
    }

    /**
     * Category转CategoryInfoDto
     *
     * @param category
     * @param childCategoryMap
     * @param articleMap
     * @return
     */
    private CategoryInfoDto categoryToCategoryInfoDto(Category category,
                                                      Map<Integer, List<Category>> childCategoryMap,
                                                      Map<Integer, List<Article>> articleMap) {
        CategoryInfoDto dto = new CategoryInfoDto();
        dto.setId(category.getId());
        dto.setName(category.getName());

        // 填入分类下的文章信息
        List<ArticleInfoDto> articleInfos = articleMap.getOrDefault(category.getId(), Collections.emptyList())
                .stream()
                .map(ArticleInfoDto::new)
                .collect(Collectors.toList());
        dto.setArticleInfos(articleInfos);

        // 递归填入分类的子分类列表
        List<Category> childCategory = childCategoryMap.getOrDefault(category.getId(), Collections.emptyList());
        List<CategoryInfoDto> childCategories = childCategory.stream()
                .map(child -> categoryToCategoryInfoDto(child, childCategoryMap, articleMap))
                .collect(Collectors.toList());
        dto.setChildCategories(childCategories);
        return dto;
    }

}
