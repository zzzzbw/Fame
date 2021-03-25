package com.zbw.fame.model.dto;

import com.zbw.fame.util.FameUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 分页Bean
 *
 * @author zzzzbw
 * @since 2017/10/23 11:44
 */
@Data
@AllArgsConstructor
public class Pagination<T> {
    private long pageNum;
    private long pageSize;
    private long total;
    private long pages;
    private String orderBy;
    private List<T> list;

    public static <S> Pagination<S> of(com.baomidou.mybatisplus.extension.plugins.pagination.Page<S> page) {
        return new Pagination<>(
                page.getCurrent(),
                page.getSize(),
                page.getTotal(),
                page.getPages(),
                FameUtils.objectToJson(page.getOrders().toString()),
                page.getRecords()
        );
    }
}
