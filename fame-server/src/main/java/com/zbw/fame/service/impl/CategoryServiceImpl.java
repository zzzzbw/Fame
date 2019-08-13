package com.zbw.fame.service.impl;

import com.zbw.fame.model.domain.Category;
import com.zbw.fame.model.domain.Middle;
import com.zbw.fame.model.domain.Post;
import com.zbw.fame.repository.CategoryRepository;
import com.zbw.fame.repository.MiddleRepository;
import com.zbw.fame.repository.PostRepository;
import com.zbw.fame.service.CategoryService;
import com.zbw.fame.service.MiddleService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.zbw.fame.service.impl.AbstractArticleServiceImpl.ARTICLE_CACHE_NAME;

/**
 * @author zhangbowen
 * @since 2019/7/19 15:56
 */
@Service
public class CategoryServiceImpl extends AbstractMetaServiceImpl<Category> implements CategoryService {


    public CategoryServiceImpl(MiddleRepository middleRepository,
                               CategoryRepository categoryRepository,
                               PostRepository postRepository,
                               MiddleService middleService) {
        super(middleRepository, categoryRepository, postRepository, middleService);
    }

    @Override
    public Category save(String name) {
        Category category = new Category();
        category.setName(name);
        return metaRepository.save(category);
    }

    @Override
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    @Transactional(rollbackFor = Throwable.class)
    public Integer delete(String name) {
        Integer metaId = super.delete(name);

        // 清除关联的文章分类
        List<Middle> middles = middleRepository.findAllByMetaId(metaId);
        for (Middle middle : middles) {
            postRepository.findById(middle.getArticleId()).ifPresent(post -> {
                post.setCategory("");
                postRepository.save(post);
            });
        }
        middleRepository.deleteAllByMetaId(metaId);
        return metaId;
    }


    @Override
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    @Transactional(rollbackFor = Throwable.class)
    public Category update(Integer id, String name) {
        Category category = super.update(id, name);

        // 更新文章中的分类列表
        Set<Integer> articleIds = middleService.getArticleIdsByMetaId(id);
        List<Post> posts = postRepository.findAllById(articleIds);
        for (Post post : posts) {
            String metaStr = post.getCategory();
            String newMetaStr = metaStr.replace(category.getName(), name);
            if (!newMetaStr.equals(metaStr)) {
                post.setCategory(newMetaStr);
                postRepository.save(post);
            }
        }
        return category;
    }

}
