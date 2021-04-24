package com.zbw.fame.controller.admin;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zbw.fame.model.dto.Pagination;
import com.zbw.fame.model.entity.Media;
import com.zbw.fame.service.MediaService;
import com.zbw.fame.util.FameConst;
import com.zbw.fame.util.RestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zzzzbw
 * @since 2019/7/9 17:34
 */
@Slf4j
@RestController
@RequestMapping("/api/admin/media")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MediaController {

    private final MediaService mediaService;

    /**
     * 分页获取媒体资源
     *
     * @param page  第几页
     * @param limit 每页数量
     * @return {@see Page<Media>}
     */
    @GetMapping
    public RestResponse<Pagination<Media>> index(@RequestParam(required = false, defaultValue = FameConst.DEFAULT_PAGE) Integer page,
                                                 @RequestParam(required = false, defaultValue = FameConst.PAGE_SIZE) Integer limit) {
        Page<Media> medias = mediaService.pageAdminMedias(page, limit);
        return RestResponse.ok(Pagination.of(medias));
    }

    /**
     * 获取媒体详情
     *
     * @param id id
     * @return Media
     */
    @GetMapping("{id}")
    public RestResponse<Media> detail(@PathVariable Integer id) {
        Media media = mediaService.getMedia(id);
        return RestResponse.ok(media);
    }

    /**
     * 上传媒体文件
     *
     * @param file 文件
     * @param name 文件名
     * @param path 文件路径
     * @return {@see Media}
     */
    @PostMapping("upload")
    public RestResponse<Media> upload(@RequestPart("file") MultipartFile file,
                                      @RequestParam String name,
                                      @RequestParam String path) {
        log.info("name:{}, path:{}", name, path);
        Media media = mediaService.upload(file, name, path);
        return RestResponse.ok(media);
    }

    /**
     * 删除媒体文件
     *
     * @param id 媒体id
     * @return {@link RestResponse#ok()}
     */
    @DeleteMapping("{id}")
    public RestResponse<RestResponse.Empty> delete(@PathVariable Integer id) {
        mediaService.delete(id);
        return RestResponse.ok();
    }
}
