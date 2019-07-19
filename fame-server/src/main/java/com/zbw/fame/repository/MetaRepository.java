package com.zbw.fame.repository;

import com.zbw.fame.model.domain.Meta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author zhangbowen
 * @since 2019/6/24 10:14
 */
public interface MetaRepository<META extends Meta> extends JpaRepository<META, Integer> {

    /**
     * 根据属性名和类型获取属性
     *
     * @param name 名称
     * @return Optional
     */
    Optional<META> findByName(String name);
}
