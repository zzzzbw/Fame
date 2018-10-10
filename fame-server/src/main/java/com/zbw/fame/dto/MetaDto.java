package com.zbw.fame.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zbw.fame.model.Metas;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 属性 Dto
 *
 * @author zbw
 * @since 2017/8/30 15:15
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class MetaDto extends Metas {

    private Integer count;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ArticleInfoDto> articles;
}
