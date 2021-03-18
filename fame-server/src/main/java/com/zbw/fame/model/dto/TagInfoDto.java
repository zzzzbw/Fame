package com.zbw.fame.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author zzzzbw
 * @since 2019/6/27 16:58
 */
@Data
public class TagInfoDto {

    private Integer id;

    private String name;

    @JsonInclude
    private List<ArticleInfoDto> articleInfos;
}
