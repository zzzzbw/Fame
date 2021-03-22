package com.zbw.fame.service;

import com.zbw.fame.BaseTest;
import com.zbw.fame.model.dto.CategoryInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author by zzzzbw
 * @since 2021/3/22 20:49
 */
@Slf4j
public class CategoryServiceTest extends BaseTest {

    @Autowired
    private CategoryServiceNew categoryServiceNew;

    @Test
    public void listCategoryInfo() {
        List<CategoryInfoDto> categoryInfoDtos = categoryServiceNew.listCategoryInfo(false);
        log.info("{}", categoryInfoDtos);
    }
}
