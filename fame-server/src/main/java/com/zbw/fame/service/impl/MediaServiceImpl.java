package com.zbw.fame.service.impl;

import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.mapper.MediaMapper;
import com.zbw.fame.model.entity.Media;
import com.zbw.fame.service.MediaService;
import com.zbw.fame.util.FameConst;
import com.zbw.fame.util.FameUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author zzzzbw
 * @since 2019/7/9 17:37
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = {@Autowired, @Lazy})
public class MediaServiceImpl extends ServiceImpl<MediaMapper, Media> implements MediaService {

    @Override
    public Page<Media> pageAdminMedias(Integer current, Integer size) {
        Page<Media> page = new Page<>(current, size);
        page.addOrder(OrderItem.desc("id"));
        return page(page);
    }

    @Override
    public Media getMedia(Integer id) {
        Media media = getById(id);
        if (null == media) {
            throw new NotFoundException(Media.class);
        }
        return media;
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Media upload(MultipartFile file, String name, String path) {
        if (null == file || file.isEmpty()) {
            throw new TipException("上传文件不能为空");
        }
        if (ObjectUtils.isEmpty(name)) {
            throw new TipException("文件名不能为空");
        }
        if (name.length() > 255) {
            throw new TipException("文件名过长");
        }

        Media media = new Media();
        try {
            Path basePath = Paths.get(path);

            Path fameDir = FameUtils.getFameDir();
            Path uploadPath = fameDir.resolve(FameConst.MEDIA_DIR);

            String suffix = FameUtils.getFileSuffix(file.getOriginalFilename());

            String mediaName = name.endsWith(suffix) ? name : name + "." + suffix;
            Path mediaUrl = basePath.resolve(mediaName);
            Path mediaPath = uploadPath.resolve(mediaUrl);
            log.info("Uploading media: [{}]", mediaPath);

            if (Files.exists(mediaPath)) {
                throw new TipException("同名文件已经存在!");
            }

            Files.createDirectories(mediaPath.getParent());
            file.transferTo(mediaPath.toFile());

            // 图片资源压缩图片
            if (Objects.requireNonNull(file.getContentType()).contains("image")) {
                String thumbnailName = name.endsWith(suffix) ?
                        FameUtils.getFileBaseName(name) + FameConst.MEDIA_THUMBNAIL_SUFFIX + "." + suffix
                        : name + FameConst.MEDIA_THUMBNAIL_SUFFIX + "." + suffix;

                Path thumbnailUrl = basePath.resolve(thumbnailName);
                Path thumbnailPath = uploadPath.resolve(thumbnailUrl);
                log.info("Compress media thumbnail: [{}]", thumbnailPath);

                FameUtils.compressImage(mediaPath.toFile(), thumbnailPath.toFile(), 0.5f);
                media.setThumbUrl(thumbnailUrl.toString());
            }

            media.setName(mediaName);
            media.setUrl(mediaUrl.toString());
            media.setSuffix(suffix);
            save(media);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new TipException(e);
        }
        return media;
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Integer id) {
        Media media = getById(id);
        if (null == media) {
            throw new NotFoundException(Media.class);
        }

        removeById(id);

        Path fameDir = FameUtils.getFameDir();
        Path uploadPath = fameDir.resolve(FameConst.MEDIA_DIR);

        Path mediaPath = uploadPath.resolve(media.getUrl());
        if (Files.exists(mediaPath)) {
            try {
                Files.delete(mediaPath);
            } catch (IOException e) {
                throw new TipException(e);
            }
        }

        if (!ObjectUtils.isEmpty(media.getThumbUrl())) {
            Path thumbnailPath = uploadPath.resolve(media.getThumbUrl());
            if (Files.exists(thumbnailPath)) {
                try {
                    Files.delete(thumbnailPath);
                } catch (IOException e) {
                    throw new TipException(e);
                }
            }
        }
    }
}
