package com.zbw.fame.service;

import com.zbw.fame.dto.SiteConfig;

/**
 * 网站设置 Service 接口
 *
 * @author zbw
 * @since 2017/10/15 22:00
 */
public interface ConfigService {

    /**
     * 获取网站配置
     *
     * @return SiteConfig
     */
    SiteConfig getSiteConfig();

    /**
     * 保存网站配置
     *
     * @param siteConfig 网站配置
     */
    void saveSiteConfig(SiteConfig siteConfig);
}
