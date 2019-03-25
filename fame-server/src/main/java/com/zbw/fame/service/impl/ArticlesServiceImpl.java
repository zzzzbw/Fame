package com.zbw.fame.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.mapper.ArticlesMapper;
import com.zbw.fame.mapper.CommentsMapper;
import com.zbw.fame.model.Articles;
import com.zbw.fame.model.Comments;
import com.zbw.fame.service.ArticlesService;
import com.zbw.fame.service.MetasService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.Types;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

/**
 * 文章 Service 实现类
 *
 * @author zbw
 * @since 2017/8/21 22:02
 */
@Slf4j
@Service("articlesService")
@Transactional(rollbackFor = Throwable.class)
public class ArticlesServiceImpl implements ArticlesService {

    private static final String ARTICLE_CACHE_NAME = "articles";

    @Autowired
    private ArticlesMapper articlesMapper;

    @Autowired
    private MetasService metasService;

    @Autowired
    private CommentsMapper commentsMapper;

    @Override
    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'article_page['+#page+':'+#limit+']'")
    public Page<Articles> getArticles(Integer page, Integer limit) {
        Articles record = new Articles();
        record.setType(Types.POST);
        return PageHelper.startPage(page, limit).doSelectPage(() -> articlesMapper.select(record));
    }

    @Override
    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'article_publish_page['+#page+':'+#limit+']'")
    public Page<Articles> getPublishArticles(Integer page, Integer limit) {
        Articles record = new Articles();
        record.setStatus(Types.PUBLISH);
        record.setType(Types.POST);
        return PageHelper.startPage(page, limit).doSelectPage(() -> articlesMapper.select(record));
    }

    @Override
    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'article_content['+#id+']'")
    public Articles get(Integer id) {
        return articlesMapper.selectByPrimaryKey(id);
    }


    @Override
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public Integer saveArticle(Articles article) {
        if (null == article) {
            throw new TipException("文章对象为空");
        }
        if (StringUtils.isEmpty(article.getTitle())) {
            throw new TipException("文章标题不能为空");
        }
        if (article.getTitle().length() > FameConsts.MAX_TITLE_COUNT) {
            throw new TipException("文章标题字数不能超过" + FameConsts.MAX_TITLE_COUNT);
        }

        if (StringUtils.isEmpty(article.getContent())) {
            throw new TipException("文章内容不能为空");
        }
        if (article.getContent().length() > FameConsts.MAX_CONTENT_COUNT) {
            throw new TipException("文章内容字数不能超过" + FameConsts.MAX_CONTENT_COUNT);
        }
        if (null == article.getAuthorId()) {
            throw new TipException("请先登陆后发布文章");
        }

        if (null != article.getId()) {
            articlesMapper.updateByPrimaryKeySelective(article);
        } else {
            article.setType(Types.POST);
            articlesMapper.insertSelective(article);
        }

        Integer id = article.getId();
        //存储分类和标签
        metasService.saveOrRemoveMetas(article.getCategory(), Types.CATEGORY, id);
        metasService.saveOrRemoveMetas(article.getTags(), Types.TAG, id);
        return id;
    }

    @Override
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public boolean updateArticle(Articles articles) {
        if (null == articles) {
            throw new TipException("文章不能为空");
        }
        return articlesMapper.updateByPrimaryKeySelective(articles) > 0;
    }

    @Override
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public boolean deleteArticle(Integer id) {
        Articles record = new Articles();
        record.setId(id);
        record.setType(Types.POST);
        Articles articles = articlesMapper.selectOne(record);
        if (null == articles) {
            throw new TipException("没有id为" + id + "的文章");
        }

        if (articlesMapper.deleteByPrimaryKey(id) > 0) {
            log.info("删除文章: {}", articles);

            // 删除文章下的评论
            Example commentsExample = Example
                    .builder(Comments.class)
                    .where(Sqls.custom().andEqualTo("articleId", id))
                    .build();
            int commentsResult = commentsMapper.deleteByExample(commentsExample);
            log.info("删除对应的评论,数量: {}", commentsResult);

            // 传空的属性，则移除该文章关联的属性
            metasService.saveOrRemoveMetas("", Types.CATEGORY, articles.getId());
            metasService.saveOrRemoveMetas("", Types.TAG, articles.getId());
            return true;
        }
        return false;
    }

    @Override
    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'article_count'")
    public Integer count() {
        Articles record = new Articles();
        record.setType(Types.POST);
        return articlesMapper.selectCount(record);
    }

    @Override
    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'article_pages['+#page+':'+#limit+']'")
    public Page<Articles> getPages(Integer page, Integer limit) {
        Articles record = new Articles();
        record.setType(Types.PAGE);
        return PageHelper.startPage(page, limit).doSelectPage(() -> articlesMapper.select(record));
    }

    @Override
    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'article_page['+#title+']'")
    public Articles getPageByTitle(String title) {
        Articles record = new Articles();
        record.setTitle(title);
        record.setType(Types.PAGE);
        record.setStatus(Types.PUBLISH);
        return articlesMapper.selectOne(record);
    }

    @Override
    @Cacheable(value = ARTICLE_CACHE_NAME, key = "'article_page['+#id+']'")
    public Articles getPageById(Integer id) {
        Articles record = new Articles();
        record.setId(id);
        record.setType(Types.PAGE);
        return articlesMapper.selectOne(record);
    }

    @Override
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public Integer savePage(Articles page) {
        if (null == page) {
            throw new TipException("自定义页面对象为空");
        }
        if (StringUtils.isEmpty(page.getTitle())) {
            throw new TipException("自定义页面标题不能为空");
        }
        if (page.getTitle().length() > FameConsts.MAX_TITLE_COUNT) {
            throw new TipException("自定义页面标题字数不能超过" + FameConsts.MAX_TITLE_COUNT);
        }

        if (StringUtils.isEmpty(page.getContent())) {
            throw new TipException("自定义页面内容不能为空");
        }
        if (page.getContent().length() > FameConsts.MAX_CONTENT_COUNT) {
            throw new TipException("自定义页面容字数不能超过" + FameConsts.MAX_CONTENT_COUNT);
        }
        if (null == page.getAuthorId()) {
            throw new TipException("请先登陆");
        }


        if (null != page.getId()) {
            articlesMapper.updateByPrimaryKeySelective(page);
        } else {
            page.setType(Types.PAGE);
            articlesMapper.insertSelective(page);
        }

        return page.getId();
    }

    @Override
    @CacheEvict(value = ARTICLE_CACHE_NAME, allEntries = true, beforeInvocation = true)
    public boolean deletePage(Integer id) {
        Articles record = new Articles();
        record.setId(id);
        record.setType(Types.PAGE);
        Articles page = articlesMapper.selectOne(record);
        if (null == page) {
            throw new TipException("没有id为" + id + "的自定义页面");
        }

        return articlesMapper.deleteByPrimaryKey(id) > 0;
    }
}
