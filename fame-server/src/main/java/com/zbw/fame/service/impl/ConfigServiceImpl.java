package com.zbw.fame.service.impl;

import com.zbw.fame.dto.SiteConfig;
import com.zbw.fame.service.ConfigService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.SystemCache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 网站设置 Service 实现类
 *
 * @author zbw
 * @since 2017/10/15 22:00
 */
@Slf4j
@Service("configService")
public class ConfigServiceImpl implements ConfigService {

    @Override
    public SiteConfig getSiteConfig() {
        SiteConfig config = SystemCache.instance().get(FameConsts.CACHE_SITE_CONFIG);
        if (null == config) {
            config = SiteConfig.builder()
                    .title(FameConsts.SITE_CONFIG_DEFAULT_TITLE)
                    .description(FameConsts.SITE_CONFIG_DEFAULT_DESCRIPTION)
                    .keywords(FameConsts.SITE_CONFIG_DEFAULT_KEYWORDS)
                    .emailSend(false)
                    .build();
        }
        return config;
    }

    @Override
    public void saveSiteConfig(SiteConfig siteConfig) {
        log.info("修改网站配置, {}", siteConfig);
        SystemCache.instance().put(FameConsts.CACHE_SITE_CONFIG, siteConfig);
    }
}
