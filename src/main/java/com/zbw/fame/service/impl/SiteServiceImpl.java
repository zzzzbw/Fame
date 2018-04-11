package com.zbw.fame.service.impl;

import com.zbw.fame.dto.SiteStatic;
import com.zbw.fame.mapper.LogsMapper;
import com.zbw.fame.service.SiteService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.SystemCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 网站设置 Service 实现类
 *
 * @author zbw
 * @create 2017/10/15 22:00
 */
@Service("siteService")
public class SiteServiceImpl implements SiteService {

    @Autowired
    private LogsMapper logsMapper;

    @Override
    public SiteStatic getSiteStatic() {
        SiteStatic siteStatic = SystemCache.instance().get(FameConsts.CACHE_SITESTATIC);
        if (null == siteStatic) {
            siteStatic = new SiteStatic();
            siteStatic.setTitle(FameConsts.SITESTATIC_DEFAULT_TITLE);
            siteStatic.setDescription(FameConsts.SITESTATIC_DEFAULT_DESCRIPTION);
            siteStatic.setKeywords(FameConsts.SITESTATIC_DEFAULT_KEYWORDS);
            siteStatic.setEmailSend(false);
        }
        return siteStatic;
    }

    @Override
    public void saveSiteStatic(SiteStatic siteStatic) {
        SystemCache.instance().put(FameConsts.CACHE_SITESTATIC, siteStatic);
    }
}
