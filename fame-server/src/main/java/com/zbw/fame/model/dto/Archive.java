package com.zbw.fame.model.dto;

import com.zbw.fame.model.domain.Article;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 归档 Dto
 *
 * @author zbw
 * @since 2017/9/21 11:24
 */
@Data
public class Archive {

    private String dateStr;

    private Date date;

    private Integer count;

    private List<Article> articles;
}
