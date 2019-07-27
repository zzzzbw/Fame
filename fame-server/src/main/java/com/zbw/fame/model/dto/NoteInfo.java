package com.zbw.fame.model.dto;

import com.zbw.fame.model.domain.Note;
import lombok.Data;

/**
 * 自定义页面Dto,通常用于前端目录
 *
 * @author zhangbowen
 * @since 2019/7/29 15:48
 */
@Data
public class NoteInfo {

    public NoteInfo() {
    }

    public NoteInfo(Note note) {
        super();
        this.id = note.getId();
        this.title = note.getTitle();
        this.priority = note.getPriority();
    }

    private Integer id;

    private String title;

    private Integer priority;
}
