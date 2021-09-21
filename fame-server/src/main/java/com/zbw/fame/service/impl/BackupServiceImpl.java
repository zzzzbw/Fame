package com.zbw.fame.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.CharsetUtil;
import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.model.entity.Article;
import com.zbw.fame.service.ArticleService;
import com.zbw.fame.service.BackupService;
import com.zbw.fame.util.FameConst;
import com.zbw.fame.util.FameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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

        List<String> lines = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            IoUtil.readUtf8Lines(inputStream, lines);

            if (!CollectionUtils.isEmpty(lines) && isTitleLine(fileBaseName, lines.get(0))) {
                // 判定第一行是否为标题，是的话去除
                lines.remove(0);
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new TipException(e);
        }

        StringJoiner content = new StringJoiner("\n");
        lines.forEach(content::add);

        if (!StringUtils.hasText(content.toString())) {
            throw new TipException("导入文件内容为空");
        }

        article.setContent(content.toString());

        articleService.updateById(article);
    }

    @Override
    public Resource exportArticle(Integer articleId) {
        Article article = articleService.getById(articleId);
        if (null == article) {
            throw new NotFoundException(Article.class);
        }

        try {
            Path filePath = Paths.get(FameConst.BACKUP_DIR, article.getTitle() + FameConst.MARKDOWN_SUFFIX);
            if (!Files.exists(filePath.getParent())) {
                Files.createDirectories(filePath.getParent());
            }


            try (final BufferedWriter writer = FileUtil.getWriter(filePath.toFile(), CharsetUtil.UTF_8, false)) {
                writer.write(article.getContent());
            }

            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new TipException(e);
        }


        throw new TipException("导出文件异常");
    }

    private boolean isTitleLine(String title, String line) {
        String condition = "# " + title;
        return Objects.equals(condition, line);
    }
}
