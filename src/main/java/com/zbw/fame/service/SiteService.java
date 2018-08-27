package com.zbw.fame.service;

import com.zbw.fame.dto.SiteStatic;

/**
 * 网站设置 Service 接口
 *
 * @author zbw
 * @since 2017/10/15 22:00
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
     * @param siteStatic 网站静态属性
     */
    void saveSiteStatic(SiteStatic siteStatic);
}
