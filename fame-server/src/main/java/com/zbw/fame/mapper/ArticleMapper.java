package com.zbw.fame.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zbw.fame.model.entity.Article;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author by zzzzbw
 * @since 2021/03/09 15:04
 */
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 增加文章点击数
     *
     * @param id           文章id
     * @param increaseHits 增加的点击数
     * @return
     */
    @Update("update article set hits = hits + #{increaseHits} where id = #{id} and deleted = 0")
    int increaseHits(@Param("id") Integer id, @Param("increaseHits") Integer increaseHits);
}
