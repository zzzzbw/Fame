package com.zbw.fame.service.impl;

import com.zbw.fame.model.domain.Middle;
import com.zbw.fame.repository.MiddleRepository;
import com.zbw.fame.service.MiddleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author zhangbowen
 * @since 2019/7/19 15:51
 */
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MiddleServiceImpl implements MiddleService {

    private final MiddleRepository middleRepository;

    @Override
    public Map<Integer, List<Middle>> getMetaIdMiddleListMap() {
        // 属性对应关联列表
        List<Middle> middles = middleRepository.findAll();
        return middles.stream().collect(Collectors.groupingBy(Middle::getMetaId));
    }

    @Override
    public Set<Integer> getMetaIdsByArticleId(Integer articleId) {
        return middleRepository.findAllByArticleId(articleId)
                .stream()
                .map(Middle::getMetaId)
                .collect(Collectors.toSet());
    }

    @Override
    public Set<Integer> getArticleIdsByMetaId(Integer metaId){
        return middleRepository.findAllByMetaId(metaId)
                .stream()
                .map(Middle::getArticleId)
                .collect(Collectors.toSet());
    }
}
