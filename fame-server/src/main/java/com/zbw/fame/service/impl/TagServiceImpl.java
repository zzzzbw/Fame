package com.zbw.fame.service.impl;

import com.zbw.fame.model.domain.Article;
import com.zbw.fame.model.domain.Middle;
import com.zbw.fame.model.domain.Tag;
import com.zbw.fame.repository.ArticleRepository;
import com.zbw.fame.repository.MiddleRepository;
import com.zbw.fame.repository.TagRepository;
import com.zbw.fame.service.MiddleService;
import com.zbw.fame.service.TagService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author zhangbowen
 * @since 2019/7/19 15:56
 */
@Service
public class TagServiceImpl extends AbstractMetaServiceImpl<Tag> implements TagService {


    public TagServiceImpl(MiddleRepository middleRepository,
                          TagRepository tagRepository,
                          ArticleRepository articleRepository,
                          MiddleService middleService) {
        super(middleRepository, tagRepository, articleRepository, middleService);
    }

    @Override
    public Tag save(String name) {
        Tag tag = new Tag();
        tag.setName(name);
        return metaRepository.save(tag);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Integer delete(String name) {
        Integer metaId = super.delete(name);

        // 清除关联的文章标签
        List<Middle> middles = middleRepository.findAllByMId(metaId);
        for (Middle middle : middles) {
            articleRepository.findById(middle.getAId()).ifPresent(article -> {
                article.setTags(this.resetTagStr(name, article.getTags()));
                articleRepository.save(article);
            });
        }
        middleRepository.deleteAllByMId(metaId);
        return metaId;
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
        public Tag update(Integer id, String name) {
        Tag tag = super.update(id, name);

        // 更新文章中的标签列表
        Set<Integer> articleIds = middleService.getArticleIdsByMetaId(id);
        List<Article> articles = articleRepository.findAllById(articleIds);
        for (Article article : articles) {
            String metaStr = article.getTags();
            String newMetaStr = metaStr.replace(tag.getName(), name);
            if (!newMetaStr.equals(metaStr)) {
                article.setTags(newMetaStr);
                articleRepository.save(article);
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
