package com.zbw.fame.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author by zzzzbw
 * @since 2021/9/21 0:06
 */
public interface BackupService {
    void importArticle(MultipartFile file, Integer articleId);
}
