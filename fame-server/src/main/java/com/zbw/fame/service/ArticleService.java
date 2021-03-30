package com.zbw.fame.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zbw.fame.model.dto.ArchiveDto;
import com.zbw.fame.model.dto.ArticleDetailDto;
import com.zbw.fame.model.dto.ArticleInfoDto;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.model.param.ArticleQuery;
import com.zbw.fame.model.param.SaveArticleParam;

import java.util.Collection;
import java.util.List;

/**
 * @author by zzzzbw
 * @since 2021/03/09 15:49
 */
public interface ArticleService extends IService<Article> {


    /**
     * 分页查询前端文章
     *
     * @param current 当前页面
     * @param size    每页数量
     * @param sort    排序
     * @return Page<ARTICLE>
     */
    IPage<ArticleDetailDto> pageArticleFront(Integer current, Integer size, List<String> sort);

    /**
     * 根据id获取前端文章
     *
     * @param id 文章url
     * @return ARTICLE
     */
    ArticleDetailDto getArticleFront(Integer id);

    /**
     * 分页查询后端文章
     *
     * @param current 当前页面
     * @param size    每页数量
     * @param query   查询条件
     * @return Page<ARTICLE>
     */
    IPage<ArticleDetailDto> pageArticleAdmin(Integer current, Integer size, ArticleQuery query);

    /**
     * 根据id获取后端文章
     *
     * @param id 文章id
     * @return ARTICLE
     */
    ArticleDetailDto getArticleAdmin(Integer id);

    /**
     * 保存或更新文章
     *
     * @param param 文章entity
     * @return Integer
     */
    ArticleDetailDto createOrUpdate(SaveArticleParam param);

    /**
     * 根据id删除文章
     *
     * @param id 文章id
     */
    void delete(Integer id);


    /**
     * 访问文章
     *
     * @param id 文章id
     */
    void visitArticle(Integer id);


    /**
     * 增加文章点击量
     *
     * @param id           文章id
     * @param increaseHits 点击量
     */
    void increaseHits(Integer id, Integer increaseHits);


    /**
     * 获取归档信息
     *
     * @return List<Archive>
     */
    List<ArchiveDto> getArchives();

    /**
     * 获取顶栏文章
     *
     * @return List<ArticleInfoDto>
     */
    List<ArticleInfoDto> listArticleHeader();

    /**
     * 通过id获取前端文章
     *
     * @param ids
     * @param isFront
     * @return
     */
    List<Article> listByIds(Collection<Integer> ids, boolean isFront);
}
