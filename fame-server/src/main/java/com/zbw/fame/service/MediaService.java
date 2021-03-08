package com.zbw.fame.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zbw.fame.model.entity.Media;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zzzzbw
 * @since 2019/7/9 17:37
 */
public interface MediaService extends IService<Media> {

    /**
     * 分页获取媒体
     *
     * @param current 第几页
     * @param size    每页数量
     * @return Page<Media>
     */
    Page<Media> pageAdminMedias(Integer current, Integer size);

    /**
     * 获取媒体详情
     *
     * @param id id
     * @return Media
     */
    Media getMedia(Integer id);

    /**
     * 上传媒体文件
     *
     * @param file 媒体文件
     * @param name 文件名
     * @param path 文件路径
     * @return
     */
    Media upload(MultipartFile file, String name, String path);

    /**
     * 删除媒体
     *
     * @param id 媒体id
     * @return 删除成功
     */
    void delete(Integer id);
}
