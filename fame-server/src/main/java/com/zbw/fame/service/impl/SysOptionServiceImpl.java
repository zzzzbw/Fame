package com.zbw.fame.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbw.fame.mapper.SysOptionMapper;
import com.zbw.fame.model.entity.SysOption;
import com.zbw.fame.service.SysOptionService;
import com.zbw.fame.util.FameUtils;
import com.zbw.fame.util.OptionKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 设置 Service 实现类
 *
 * @author zzzzbw
 * @since 2019-05-20 22:45
 */
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class SysOptionServiceImpl extends ServiceImpl<SysOptionMapper, SysOption> implements SysOptionService {

    @Override
    public Map<String, String> getAllOptionMap() {
        return lambdaQuery().list()
                .stream()
                .collect(Collectors.toMap(SysOption::getOptionKey, SysOption::getOptionValue));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(String key, T defaultValue) {
        SysOption sysOption = lambdaQuery()
                .eq(SysOption::getOptionKey, key)
                .one();

        return (T) (sysOption == null || ObjectUtils.isEmpty(sysOption.getOptionValue()) ?
                defaultValue :
                FameUtils.convertStrTo(sysOption.getOptionValue(), defaultValue.getClass()));
    }

    @Override
    public String get(String key) {
        return this.get(key, "");
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void save(String key, String value) {
        SysOption sysOption =  lambdaQuery()
                .eq(SysOption::getOptionKey, key)
                .one();
        if (null == sysOption) {
            sysOption = new SysOption();
            sysOption.setOptionKey(key);
        }
        sysOption.setOptionValue(value);
        saveOrUpdate(sysOption);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void save(Map<String, String> options) {
        options.forEach(this::save);
    }

    @Override
    public Map<String, String> getFrontOptionMap() {
        Map<String, String> frontOptions = new HashMap<>(16);
        Map<String, String> allOptions = getAllOptionMap();
        OptionKeys.FRONT_OPTION_KEYS.forEach(key -> frontOptions.put(key, allOptions.getOrDefault(key, "")));
        return frontOptions;
    }

}
