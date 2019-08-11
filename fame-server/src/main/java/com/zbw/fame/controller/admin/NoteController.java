package com.zbw.fame.controller.admin;

import com.zbw.fame.model.domain.Note;
import com.zbw.fame.model.domain.User;
import com.zbw.fame.model.enums.ArticleStatus;
import com.zbw.fame.service.NoteService;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.RestResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangbowen
 * @since 2019/7/22 17:50
 */
@RestController
@RequestMapping("/api/admin/note")
public class NoteController extends AbstractArticleController<Note> {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        super(noteService);
        this.noteService = noteService;
    }

    /**
     * 新建或修改自定义页面
     *
     * @param id      自定义页面id
     * @param title   标题
     * @param content 内容
     * @param status  页面状态 {@link ArticleStatus}
     * @return {@see String}
     */
    @PostMapping
    public RestResponse<Integer> save(@RequestParam(value = "id", required = false) Integer id,
                             @RequestParam(value = "title") String title,
                             @RequestParam(value = "content") String content,
                             @RequestParam(value = "status") ArticleStatus status,
                             @RequestParam(value = "priority", defaultValue = "0") Integer priority,
                             @RequestParam(value = "allowComment", defaultValue = "false") Boolean allowComment) {
        User user = FameUtil.getLoginUser();
        Note note = new Note();
        if (!StringUtils.isEmpty(id)) {
            note.setId(id);
        }
        note.setTitle(title);
        note.setContent(content);
        note.setStatus(status);
        note.setPriority(priority);
        note.setAllowComment(allowComment);
        note.setAuthorId(user.getId());
        Integer noteId = noteService.save(note);
        return RestResponse.ok(noteId);
    }
}
