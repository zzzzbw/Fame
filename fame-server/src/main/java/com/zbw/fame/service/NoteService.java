package com.zbw.fame.service;

import com.zbw.fame.model.domain.Note;
import com.zbw.fame.model.dto.NoteInfo;

import java.util.List;

/**
 * @author zbw
 * @since 2019/7/21 20:01
 */
public interface NoteService extends ArticleService<Note> {

    /**
     * 获取前端自定义页面信息列表
     *
     * @return List<NoteInfo>
     */
    List<NoteInfo> getFrontNoteList();
}
