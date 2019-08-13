package com.zbw.fame.service.impl;

import com.zbw.fame.exception.NotFoundException;
import com.zbw.fame.exception.TipException;
import com.zbw.fame.model.domain.Media;
import com.zbw.fame.repository.MediaRepository;
import com.zbw.fame.service.MediaService;
import com.zbw.fame.util.FameConsts;
import com.zbw.fame.util.FameUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author zhangbowen
 * @since 2019/7/9 17:37
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MediaServiceImpl implements MediaService {

    private final MediaRepository mediaRepository;

    @Override
    public Page<Media> pageAdminMedias(Integer page, Integer limit) {
        return mediaRepository.findAll(PageRequest.of(page, limit, FameUtil.sortDescById()));
    }

    @Override
    public Media getMedia(Integer id) {
        return mediaRepository.findById(id).orElseThrow(() -> new TipException("媒体不存在"));
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Media upload(MultipartFile file, String name, String path) {
        if (null == file || file.isEmpty()) {
            throw new TipException("上传文件不能为空");
        }
        if (StringUtils.isEmpty(name)) {
            throw new TipException("文件名不能为空");
        }
        if (name.length() > 255) {
            throw new TipException("文件名过长");
        }

        Media media = new Media();
        try {
            Path basePath = Paths.get(path);

            Path fameDir = FameUtil.getFameDir();
            Path uploadPath = fameDir.resolve(FameConsts.MEDIA_DIR);

            String suffix = FameUtil.getFileSuffix(file.getOriginalFilename());

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
                        FameUtil.getFileBaseName(name) + FameConsts.MEDIA_THUMBNAIL_SUFFIX + "." + suffix
                        : name + FameConsts.MEDIA_THUMBNAIL_SUFFIX + "." + suffix;

                Path thumbnailUrl = basePath.resolve(thumbnailName);
                Path thumbnailPath = uploadPath.resolve(thumbnailUrl);
                log.info("Compress media thumbnail: [{}]", thumbnailPath);

                FameUtil.compressImage(mediaPath.toFile(), thumbnailPath.toFile(), 0.5f);
                media.setThumbUrl(thumbnailUrl.toString());
            }

            media.setName(mediaName);
            media.setUrl(mediaUrl.toString());
            media.setSuffix(suffix);
            mediaRepository.save(media);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new TipException(e);
        }
        return media;
    }


    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Integer id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Media.class));

        mediaRepository.delete(media);

        Path fameDir = FameUtil.getFameDir();
        Path uploadPath = fameDir.resolve(FameConsts.MEDIA_DIR);

        Path mediaPath = uploadPath.resolve(media.getUrl());
        if (Files.exists(mediaPath)) {
            try {
                Files.delete(mediaPath);
            } catch (IOException e) {
                throw new TipException(e);
            }
        }

        if (!StringUtils.isEmpty(media.getThumbUrl())) {
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
