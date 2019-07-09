package com.zbw.fame.repository;

import com.zbw.fame.model.domain.Media;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhangbowen
 * @since 2019/7/09 17:45
 */
public interface MediaRepository extends JpaRepository<Media, Integer> {
}
