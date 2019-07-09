package com.fame.zbw;

import com.zbw.fame.service.MediaService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.FameUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author zhangbowen
 * @since 2019/7/9 17:58
 */
@Slf4j
public class MediaServiceTests extends BaseTests {

    @Autowired
    private MediaService mediaService;

    @Test
    public void test1() {
        MultipartFile file = new MultipartFile() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public String getOriginalFilename() {
                return "abc.txt";
            }

            @Override
            public String getContentType() {
                return "123123";
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 0;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[0];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
        mediaService.upload(file, "ttt.txt", "a/b/c");
    }

    @Test
    public void test2() {
        Path dir = Paths.get(FameConsts.USER_HOME, FameConsts.FAME_HOME, "aaaa");
        System.out.println(dir);
        System.out.println(dir.isAbsolute());
    }

    @Test
    public void test3() throws IOException {
        Path dir = Paths.get("E:\\zbw\\develop\\java\\Fame\\fame-server\\upload\\a\\b\\c", "ccc.jpg");
        Path target = Paths.get("E:\\zbw\\develop\\java\\Fame\\fame-server\\upload\\a\\b\\c", "ccc_s.jpg");
        System.out.println(dir);
        System.out.println(dir.isAbsolute());
        FameUtil.compressImage(dir.toFile(), target.toFile(), 0.5f);
    }

    @Test
    public void test4() throws IOException {
        Path dir = Paths.get("E:\\zbw\\develop\\java\\Fame\\fame-server\\upload\\a\\b\\e", "ccc.jpg");
        Files.createDirectories(dir.getParent());
        System.out.println(dir);
    }

    @Test
    public void test5() {
        String result = StringUtils.cleanPath("E:\\zbw\\develop\\java\\Fame\\fame-server\\upload\\a\\b\\e\\ccc.jpg");
        log.info("result:{}", result);
    }
}
