package com.zbw.fame.util;

import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.MySqlMapper;

/**
 * Created by zbw on 2017/7/5.
 */
public interface MyMapper<T> extends Mapper<T>, MySqlMapper<T> {
}
