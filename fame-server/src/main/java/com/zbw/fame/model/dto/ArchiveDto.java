package com.zbw.fame.model.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author by zzzzbw
 * @since 2021/03/10 14:39
 */
@Data
public class ArchiveDto {

    private String year;

    private List<ArticleInfoDto> articleInfos;
}
