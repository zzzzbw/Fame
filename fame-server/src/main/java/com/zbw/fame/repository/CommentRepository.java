package com.zbw.fame.repository;

import com.zbw.fame.model.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhangbowen
 * @since 2019/6/24 10:14
 */
public interface CommentRepository extends JpaRepository<Comment, Integer> {
}
