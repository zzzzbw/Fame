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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.Date;

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

    @Autowired
    private ArticlesMapper articlesMapper;

    @Autowired
    private MetasService metasService;

    @Autowired
    private CommentsMapper commentsMapper;

    @Override
    public Page<Articles> getArticles(Integer page, Integer limit) {
        Articles record = new Articles();
        record.setType(Types.POST);
        return PageHelper.startPage(page, limit).doSelectPage(() -> articlesMapper.select(record));
    }

    @Override
    public Page<Articles> getContents(Integer page, Integer limit) {
        Articles record = new Articles();
        record.setStatus(Types.PUBLISH);
        record.setType(Types.POST);
        return PageHelper.startPage(page, limit).doSelectPage(() -> articlesMapper.select(record));
    }

    @Override
    public Articles get(Integer id) {
        return articlesMapper.selectByPrimaryKey(id);
    }


    @Override
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

        article.setModified(new Date());

        if (null != article.getId()) {
            articlesMapper.updateByPrimaryKeySelective(article);
        } else {
            article.setCreated(new Date());
            article.setHits(0);
            article.setType(Types.POST);
            articlesMapper.insert(article);
        }

        Integer id = article.getId();
        //存储分类和标签
        metasService.saveOrRemoveMetas(article.getCategory(), Types.CATEGORY, id);
        metasService.saveOrRemoveMetas(article.getTags(), Types.TAG, id);
        return id;
    }

    @Override
    public boolean updateArticle(Articles articles) {
        if (null == articles) {
            throw new TipException("文章不能为空");
        }
        return articlesMapper.updateByPrimaryKeySelective(articles) > 0;
    }

    @Override
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
    public Integer count() {
        Articles record = new Articles();
        record.setType(Types.POST);
        return articlesMapper.selectCount(record);
    }

    @Override
    public Page<Articles> getPages(Integer page, Integer limit) {
        Articles record = new Articles();
        record.setType(Types.PAGE);
        return PageHelper.startPage(page, limit).doSelectPage(() -> articlesMapper.select(record));
    }

    @Override
    public Articles getPage(String title) {
        Articles record = new Articles();
        record.setTitle(title);
        record.setType(Types.PAGE);
        record.setStatus(Types.PUBLISH);
        return articlesMapper.selectOne(record);
    }

    @Override
    public Articles getPage(Integer id) {
        Articles record = new Articles();
        record.setId(id);
        record.setType(Types.PAGE);
        return articlesMapper.selectOne(record);
    }

    @Override
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


        page.setModified(new Date());

        if (null != page.getId()) {
            articlesMapper.updateByPrimaryKeySelective(page);
        } else {
            page.setCreated(new Date());
            page.setType(Types.PAGE);
            articlesMapper.insert(page);
        }

        return page.getId();
    }

    @Override
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
