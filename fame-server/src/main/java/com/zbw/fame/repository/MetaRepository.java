package com.zbw.fame.repository;

import com.zbw.fame.model.domain.Meta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author zhangbowen
 * @since 2019/6/24 10:14
 */
public interface MetaRepository extends JpaRepository<Meta, Integer> {

    /**
     * 根据类型查询属性列表
     *
     * @param type 类型
     * @return List<Meta>
     */
    List<Meta> findAllByType(String type);

    /**
     * 根据属性名和类型获取属性
     *
     * @param name 名称
     * @param type 类型
     * @return Optional
     */
    Optional<Meta> findByNameAndType(String name, String type);
}
