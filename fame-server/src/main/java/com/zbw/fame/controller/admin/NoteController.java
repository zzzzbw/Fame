package com.zbw.fame.controller.admin;

import com.zbw.fame.model.domain.Note;
import com.zbw.fame.model.domain.User;
import com.zbw.fame.model.param.SaveNoteParam;
import com.zbw.fame.service.NoteService;
import com.zbw.fame.util.FameUtils;
import com.zbw.fame.util.RestResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zzzzbw
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
     * @return {@see String}
     */
    @PostMapping
    public RestResponse<Integer> save(SaveNoteParam param) {
        Note note = FameUtils.convertTo(param, Note.class);
        User user = FameUtils.getLoginUser();
        note.setAuthorId(user.getId());
        Integer noteId = noteService.save(note);
        return RestResponse.ok(noteId);
    }
}
