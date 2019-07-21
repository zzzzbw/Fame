package com.zbw.fame.controller.admin;

import com.zbw.fame.model.domain.Tag;
import com.zbw.fame.service.TagService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 标签管理 Controller
 *
 * @author zbw
 * @since 2019/7/21 10:14
 */
@RestController
@RequestMapping("/api/admin/tag")
public class TagController extends AbstractMetaController<Tag> {

    public TagController(TagService tagService) {
        super(tagService);
    }
}
