package com.zbw.fame.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Mapper插件通用接口
 *
 * @author zbw
 * @since 2017/7/5.
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
