package com.zbw.fame.repository;

import com.zbw.fame.model.domain.Log;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author zhangbowen
 * @since 2019/6/24 10:14
 */
public interface LogRepository extends JpaRepository<Log, Integer> {
}
