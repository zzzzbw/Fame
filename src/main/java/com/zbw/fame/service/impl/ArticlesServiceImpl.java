package com.zbw.fame.service.impl;

import com.github.pagehelper.PageHelper;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.mapper.ArticlesMapper;
import com.zbw.fame.model.Articles;
import com.zbw.fame.service.ArticlesService;
import com.zbw.fame.service.MetasService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * 文章 Service 实现类
 *
 * @auther zbw
 * @create 2017/8/21 22:02
 */
@Service("articlesService")
public class ArticlesServiceImpl implements ArticlesService {

    @Autowired
    private ArticlesMapper articlesMapper;

    @Autowired
    private MetasService metasService;

    @Override
    public List<Articles> getArticles(Integer page) {
        PageHelper.startPage(page, FameConsts.PAGE_SIZE);
        return articlesMapper.selectAll();
    }

    @Override
    public List<Articles> getContents(Integer page) {
        PageHelper.startPage(page, FameConsts.PAGE_SIZE);
        Articles article = new Articles();
        article.setStatus(Types.PUBLISH);
        return articlesMapper.select(article);
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
            articlesMapper.updateByPrimaryKey(article);
        } else {
            article.setCreated(new Date());
            article.setHits(0);
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
        Articles articles = articlesMapper.selectByPrimaryKey(id);
        if (null == articles) {
            throw new TipException("没有id为" + id + "的文章");
        }

        if (articlesMapper.deleteByPrimaryKey(id) > 0) {
            //传空的属性，则移除该文章关联的属性
            metasService.saveOrRemoveMetas("", Types.CATEGORY, articles.getId());
            metasService.saveOrRemoveMetas("", Types.TAG, articles.getId());
            return true;
        }
        return false;
    }

    @Override
    public Integer count() {
        return articlesMapper.selectCount(new Articles());
    }

}
