package com.zbw.fame.service;

import com.zbw.fame.dto.SiteStatic;

/**
 * 网站设置 Service 接口
 *
 * @author zbw
 * @create 2017/10/15 22:00
 */
public interface SiteService {

    /**
     * 获取网站静态属性
     *
     * @return SiteStatic
     */
    SiteStatic getSiteStatic();

    /**
     * 保存网站静态属性
     *
     * @param title 网站title
     * @param description 网站description
     * @param keywords 网站keywords
     */
    void saveSiteStatic(String title, String description, String keywords);
}
