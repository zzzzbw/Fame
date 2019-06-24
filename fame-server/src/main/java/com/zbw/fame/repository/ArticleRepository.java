package com.zbw.fame.repository;

import com.zbw.fame.model.domain.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * @author zhangbowen
 * @since 2019/6/24 10:14
 */
public interface ArticleRepository extends JpaRepository<Article, Integer>, JpaSpecificationExecutor<Article> {

    /**
     * 根据状态和类型查询文章
     *
     * @param status 状态
     * @param type   类型
     * @return List<Article>
     */
    List<Article> findAllByStatusAndType(String status, String type);

    /**
     * 根据状态和类型查询文章
     *
     * @param notStatus 不属于的状态
     * @param type      类型
     * @return List<Article>
     */
    List<Article> findAllByStatusNotAndType(String notStatus, String type);

}
