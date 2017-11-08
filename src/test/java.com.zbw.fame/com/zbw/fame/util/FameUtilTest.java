package com.zbw.fame.util;

import org.junit.Test;

/**
 * Created by zbw on 2017/10/25.
 */
public class FameUtilTest {

    @Test
    public void getMd5() throws Exception {
        String password = "123456";
        String md5 = FameUtil.getMd5(password);
        System.out.print(md5);
    }

}