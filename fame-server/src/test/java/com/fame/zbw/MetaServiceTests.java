package com.fame.zbw;

import com.zbw.fame.model.dto.MetaInfo;
import com.zbw.fame.repository.MetaRepository;
import com.zbw.fame.service.MetaService;
import com.zbw.fame.util.RestResponse;
import com.zbw.fame.util.Types;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author zhangbowen
 * @since 2019/6/24 13:54
 */
@Slf4j
public class MetaServiceTests extends BaseTests {

    @Autowired
    private MetaService metaService;

    @Autowired
    private MetaRepository metaRepository;

    @Test
    public void test1() {
        metaService.deleteMeta("testCategory", Types.CATEGORY);
    }

    @Test
    public void test3() {
        List<MetaInfo> metaInfos = metaService.getMetaInfos(Types.CATEGORY);
        log.info("{}", metaInfos);
    }

}
