package com.zbw.fame.repository;

import com.zbw.fame.model.domain.SysOption;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhangbowen
 * @since 2019/6/24 10:19
 */
public interface OptionRepository extends JpaRepository<SysOption, Integer> {

    /**
     * 根据Option key获取
     *
     * @param key key
     * @return SysOption
     */
    SysOption findByOptionKey(String key);
}
