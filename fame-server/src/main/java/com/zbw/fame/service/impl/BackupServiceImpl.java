package com.zbw.fame.service.impl;

import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.service.ArticleService;
import com.zbw.fame.service.BackupService;
import com.zbw.fame.util.FameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @author by zzzzbw
 * @since 2021/9/21 0:06
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class BackupServiceImpl implements BackupService {

    private final ArticleService articleService;

    @Transactional
    @Override
    public void importArticle(MultipartFile file, Integer articleId) {

        if (file.isEmpty()) {
            throw new TipException("上传文件不能为空 ");
        }


        Article article = articleService.getById(articleId);
        if (null == article) {
            throw new NotFoundException(Article.class);
        }


        String fileBaseName = FameUtils.getFileBaseName(file.getOriginalFilename());
        article.setTitle(fileBaseName);

        StringJoiner content = new StringJoiner("\n");
        try (InputStream inputStream = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            if (reader.ready()) {
                // 判定第一行是否和标题相同，相同的话则跳过
                String condition1 = "# " + fileBaseName;
                String condition2 = "#" + fileBaseName;
                String line = reader.readLine();
                if (!Objects.equals(condition1, line) && !Objects.equals(condition2, line)) {
                    content.add(line);
                }
            }

            while (reader.ready()) {
                String line = reader.readLine();
                content.add(line);
            }

            article.setContent(content.toString());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new TipException(e);
        }

        articleService.updateById(article);
    }
}
