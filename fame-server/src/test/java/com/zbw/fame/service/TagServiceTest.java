package com.zbw.fame.service;

import com.zbw.fame.BaseTest;
import com.zbw.fame.model.dto.TagInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author by zzzzbw
 * @since 2021/3/22 20:50
 */
@Slf4j
public class TagServiceTest extends BaseTest {

    @Autowired
    private TagService tagService;

    @Test
    public void listTagInfo() {
        List<TagInfoDto> tagInfoDtos = tagService.listTagInfo(false);
        log.info("{}", tagInfoDtos);
    }
}
