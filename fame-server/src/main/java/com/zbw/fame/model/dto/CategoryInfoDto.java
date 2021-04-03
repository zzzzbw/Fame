package com.zbw.fame.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * @author by zzzzbw
 * @since 2021/03/19 10:55
 */
@Data
public class CategoryInfoDto {


    private Integer id;

    private String name;
    
    private List<CategoryInfoDto> childCategories;

    @JsonInclude
    private List<ArticleInfoDto> articleInfos;

}
