package com.zbw.fame.dto;

import com.github.pagehelper.Page;

import java.util.List;

/**
 * 分页Bean
 *
 * @author zbw
 * @create 2017/10/23 11:44
 */
public class Pagination<T> {
    private int pageNum;
    private int pageSize;
    private long total;
    private int pages;
    private boolean count;
    private String orderBy;
    private List<T> list;

    public Pagination() {
    }

    @SuppressWarnings("unchecked")
    public Pagination(Page page) {
        pageNum = page.getPageNum();
        pageSize = page.getPageSize();
        total = page.getTotal();
        pages =page.getPages();
        count = page.isCount();
        orderBy = page.getOrderBy();
        list = page.getResult();
    }


    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public boolean isCount() {
        return count;
    }

    public void setCount(boolean count) {
        this.count = count;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
