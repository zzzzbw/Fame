package com.zbw.fame.service;

import com.zbw.fame.BaseTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author zzzzbw
 * @since 2019/6/24 13:54
 */
@Slf4j
public class MetaServiceTest extends BaseTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private TagService tagService;

    @Test
    public void test1() {
        categoryService.save("testCategory");
    }

    @Test
    public void test2() {
        categoryService.delete("First");
    }

    @Test
    public void test3(){

    }

}
