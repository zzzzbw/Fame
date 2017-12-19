package com.zbw.fame.service.impl;

import com.zbw.fame.Application;
import com.zbw.fame.service.MetasService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by zbw on 2017/12/19.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class MetasServiceImplTest {

    @Autowired
    MetasService metasService;

    @Test
    public void getMetaDtos() throws Exception {
    }

    @Test
    public void getMetaDto() throws Exception {
    }

    @Test
    public void deleteMeta() throws Exception {
        metasService.deleteMeta("test", "tag");
    }

    @Test
    public void saveMeta() throws Exception {
        metasService.saveMeta("test","tag");
    }

    @Test
    public void updateMeta() throws Exception {
    }

    @Test
    public void saveOrRemoveMetas() throws Exception {
    }

}