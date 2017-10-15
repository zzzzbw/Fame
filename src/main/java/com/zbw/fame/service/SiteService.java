package com.zbw.fame.service;

import com.zbw.fame.dto.SiteStatic;

/**
 * 网站设置 Service 接口
 *
 * @auther zbw
 * @create 2017/10/15 22:00
 */
public interface SiteService {

    Integer getVisit();

    SiteStatic getSiteStatic();

    void saveSiteStatic(String title, String description, String keywords);
}
