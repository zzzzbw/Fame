package com.zbw.fame.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zhangbowen
 * @since 2019/6/27 16:58
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetaInfo {

    private Integer id;

    private String name;

    private Integer articleCount;

    @JsonInclude
    private List<ArticleInfo> articles;
}
