package com.zbw.fame.service.impl;

import com.zbw.fame.model.domain.Middle;
import com.zbw.fame.model.domain.Post;
import com.zbw.fame.model.domain.Tag;
import com.zbw.fame.repository.MiddleRepository;
import com.zbw.fame.repository.PostRepository;
import com.zbw.fame.repository.TagRepository;
import com.zbw.fame.service.MiddleService;
import com.zbw.fame.service.TagService;
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
public class TagServiceImpl extends AbstractMetaServiceImpl<Tag> implements TagService {


    public TagServiceImpl(MiddleRepository middleRepository,
                          TagRepository tagRepository,
                          PostRepository postRepository,
                          MiddleService middleService) {
        super(middleRepository, tagRepository, postRepository, middleService);
    }

    @Override
    public Tag save(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        return metaRepository.save(tag);
    }

    @Override
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    @Transactional(rollbackFor = Throwable.class)
    public Integer delete(String name) {
        Integer metaId = super.delete(name);

        // 清除关联的文章标签
        List<Middle> middles = middleRepository.findAllByMetaId(metaId);
        for (Middle middle : middles) {
            postRepository.findById(middle.getArticleId()).ifPresent(article -> {
                article.setTags(this.resetTagStr(name, article.getTags()));
                postRepository.save(article);
            });
        }
        middleRepository.deleteAllByMetaId(metaId);
        return metaId;
    }


    @Override
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    @Transactional(rollbackFor = Throwable.class)
        public Tag update(Integer id, String name) {
        Tag tag = super.update(id, name);

        // 更新文章中的标签列表
        Set<Integer> articleIds = middleService.getArticleIdsByMetaId(id);
        List<Post> posts = postRepository.findAllById(articleIds);
        for (Post post : posts) {
            String metaStr = post.getTags();
            String newMetaStr = metaStr.replace(tag.getName(), name);
            if (!newMetaStr.equals(metaStr)) {
                post.setTags(newMetaStr);
                postRepository.save(post);
            }
        }
        return tag;
    }


    /**
     * 从标签字符串中去除一个属性
     *
     * @param name 标签名
     * @param tagStr 标签字符串
     * @return
     */
    private String resetTagStr(String name, String tagStr) {
        String[] tagArr = tagStr.split(",");
        StringBuilder sb = new StringBuilder();
        for (String tag : tagArr) {
            if (!name.equals(tag)) {
                sb.append(",").append(tag);
            }
        }
        if (sb.length() > 0) {
            return sb.substring(1);
        }
        return "";
    }

}
