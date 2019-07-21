package com.zbw.fame.model.dto;

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

    private String year;

    private List<PostInfo> postInfos;
}
