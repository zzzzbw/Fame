package com.zbw.fame.service.impl;

import com.github.pagehelper.PageHelper;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.mapper.ArticlesMapper;
import com.zbw.fame.model.Articles;
import com.zbw.fame.service.ArticlesService;
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

    @Override
    public List<Articles> getArticles(Integer page) {
        PageHelper.startPage(page, FameConsts.PAGE_SIZE);
        List<Articles> articles = articlesMapper.selectAll();
        return articles;
    }

    @Override
    public List<Articles> getContents(Integer page) {
        PageHelper.startPage(page, FameConsts.PAGE_SIZE);
        Articles article = new Articles();
        article.setStatus(Types.PUBLISH);
        List<Articles> articles = articlesMapper.select(article);
        return articles;
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
        if (null == article.getAuthor_id()) {
            throw new TipException("请先登陆后发布文章");
        }

        article.setCreated(new Date());
        article.setModified(new Date());

        Integer id;
        if (null != article.getId()) {
            id = articlesMapper.updateByPrimaryKey(article);
        } else {
            id = articlesMapper.insert(article);
        }


        //存储分类和标签
        //metasService.saveMetas(cid, tags, Types.TAG);
        //metasService.saveMetas(cid, categories, Types.CATEGORY);
        return id;
    }

    @Override
    public boolean deleteArticle(Integer id) {
        return articlesMapper.deleteByPrimaryKey(id) > 0;
    }

}
