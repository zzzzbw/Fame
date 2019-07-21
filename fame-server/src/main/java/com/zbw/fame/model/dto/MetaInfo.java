package com.zbw.fame.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author zhangbowen
 * @since 2019/6/27 16:58
 */
@Data
public class MetaInfo {

    private Integer id;

    private String name;

    @JsonInclude
    private List<PostInfo> postInfos;
}
