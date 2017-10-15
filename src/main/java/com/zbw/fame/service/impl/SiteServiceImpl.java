package com.zbw.fame.service.impl;

import com.zbw.fame.dto.SiteStatic;
import com.zbw.fame.mapper.LogsMapper;
import com.zbw.fame.model.Logs;
import com.zbw.fame.service.SiteService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.SystemCache;
import com.zbw.fame.util.Types;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 网站设置 Service 实现类
 *
 * @auther zbw
 * @create 2017/10/15 22:00
 */
@Service("siteService")
public class SiteServiceImpl implements SiteService {

    @Autowired
    private LogsMapper logsMapper;


    @Override
    public Integer getVisit() {
        Logs condition = new Logs();
        condition.setType(Types.LOG_TYPE_VISIT);
        List<Logs> logs = logsMapper.select(condition);
        Integer visit = 0;
        try {
            for (Logs log : logs) {
                visit += Integer.parseInt(log.getData());
            }
        } catch (NumberFormatException e) {
            visit = logs.size() * FameConsts.CACHE_ROUTE_VISIT_SAVE;
        }
        return visit;
    }

    @Override
    public SiteStatic getSiteStatic() {
        SiteStatic siteStatic = SystemCache.instance().get(FameConsts.CACHE_SITESTATIC);
        if (null == siteStatic) {
            siteStatic = new SiteStatic();
            siteStatic.setTitle(FameConsts.SITESTATIC_DEFAULT_TITLE);
            siteStatic.setDescription(FameConsts.SITESTATIC_DEFAULT_DESCRIPTION);
            siteStatic.setKeywords(FameConsts.SITESTATIC_DEFAULT_KEYWORDS);
        }
        return siteStatic;
    }

    @Override
    public void saveSiteStatic(String title, String description, String keywords) {
        SiteStatic siteStatic = new SiteStatic();
        siteStatic.setTitle(title);
        siteStatic.setDescription(description);
        siteStatic.setKeywords(keywords);
        SystemCache.instance().put(FameConsts.CACHE_SITESTATIC, siteStatic);
    }
}
