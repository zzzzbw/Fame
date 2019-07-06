package com.zbw.fame.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * 分页Bean
 *
 * @author zbw
 * @since 2017/10/23 11:44
 */
@Data
@AllArgsConstructor
public class Pagination<T> {
    private int pageNum;
    private int pageSize;
    private long total;
    private int pages;
    private String orderBy;
    private List<T> list;

    @SuppressWarnings("unchecked")
    public static <S> Pagination<S> of(Page<S> page) {
        return new Pagination(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.getSort().toString(),
                page.getContent()
        );
    }
}
