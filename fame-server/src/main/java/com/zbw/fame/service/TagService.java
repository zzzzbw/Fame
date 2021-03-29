package com.zbw.fame.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zbw.fame.model.dto.TagInfoDto;
import com.zbw.fame.model.entity.Tag;
import com.zbw.fame.model.param.SaveTagParam;

import java.util.List;

/**
 * @author by zzzzbw
 * @since 2021/03/15 11:30
 */
public interface TagService extends IService<Tag> {
    /**
     * 删除标签
     *
     * @param id 属性名
     */
    void delete(Integer id);

    /**
     * 保存或更新标签
     *
     * @param param
     * @return
     */
    Tag createOrUpdate(SaveTagParam param);

    /**
     * 获取标签信息
     *
     * @return List<TagInfoDto>
     * @param isFront 是否为前端标签
     */
    List<TagInfoDto> listTagInfo(boolean isFront);
}
