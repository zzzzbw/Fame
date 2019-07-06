package com.zbw.fame.repository;

import com.zbw.fame.model.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author zhangbowen
 * @since 2019/6/24 10:14
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * 根据用户名查询用户
     * @param username 用户名
     * @return User
     */
    User findByUsername(String username);

    /**
     * 根据用户名或邮箱查询用户
     *
     * @param username 用户名
     * @param email    邮箱
     * @return User
     */
    User findByUsernameOrEmail(String username, String email);
}
