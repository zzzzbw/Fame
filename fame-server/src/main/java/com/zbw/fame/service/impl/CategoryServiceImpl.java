package com.zbw.fame.service.impl;

import com.zbw.fame.model.domain.Article;
import com.zbw.fame.model.domain.Category;
import com.zbw.fame.model.domain.Middle;
import com.zbw.fame.repository.ArticleRepository;
import com.zbw.fame.repository.CategoryRepository;
import com.zbw.fame.repository.MiddleRepository;
import com.zbw.fame.service.CategoryService;
import com.zbw.fame.service.MiddleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author zhangbowen
 * @since 2019/7/19 15:56
 */
@Service
public class CategoryServiceImpl extends AbstractMetaServiceImpl<Category> implements CategoryService {


    public CategoryServiceImpl(MiddleRepository middleRepository,
                               CategoryRepository categoryRepository,
                               ArticleRepository articleRepository,
                               MiddleService middleService) {
        super(middleRepository, categoryRepository, articleRepository, middleService);
    }

    @Override
    public Category save(String name) {
        Category category = new Category();
        category.setName(name);
        return metaRepository.save(category);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Integer delete(String name) {
        Integer metaId = super.delete(name);

        // 清除关联的文章分类
        List<Middle> middles = middleRepository.findAllByMId(metaId);
        for (Middle middle : middles) {
            articleRepository.findById(middle.getAId()).ifPresent(article -> {
                article.setCategory("");
                articleRepository.save(article);
            });
        }
        middleRepository.deleteAllByMId(metaId);
        return metaId;
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Category update(Integer id, String name) {
        Category category = super.update(id, name);

        // 更新文章中的分类列表
        Set<Integer> articleIds = middleService.getArticleIdsByMetaId(id);
        List<Article> articles = articleRepository.findAllById(articleIds);
        for (Article article : articles) {
            String metaStr = article.getCategory();
            String newMetaStr = metaStr.replace(category.getName(), name);
            if (!newMetaStr.equals(metaStr)) {
                article.setCategory(newMetaStr);
                articleRepository.save(article);
            }
        }
        return category;
    }

}
