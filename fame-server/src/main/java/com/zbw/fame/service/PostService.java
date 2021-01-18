package com.zbw.fame.service;

import com.zbw.fame.model.domain.Post;
import com.zbw.fame.model.dto.Archive;

import java.util.List;

/**
 * @author zbw
 * @since 2019/7/21 20:01
 */
public interface PostService extends ArticleService<Post> {

    /**
     * 访问文章
     *
     * @param postId 文章id
     */
    void visitPost(Integer postId);


    /**
     * 增加文章点击量
     *
     * @param postId 文章id
     * @param hits   点击量
     */
    void increaseHits(Integer postId, Integer hits);


    /**
     * 获取归档信息
     *
     * @return List<Archive>
     */
    List<Archive> getArchives();
}
