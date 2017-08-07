package com.zbw.fame.controller.admin;

import com.zbw.fame.controller.BaseController;
import com.zbw.fame.model.Articles;
import com.zbw.fame.util.RestResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 后台文章管理 Controller
 *
 * @auther zbw
 * @create 2017/7/11 19:52
 */
@RestController
@RequestMapping("/api/admin/article")
public class ArticleController extends BaseController{

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public RestResponse saveArticle(Articles article){
        Articles s=article;
        System.out.print(s.getTitle());
        return RestResponse.ok("保存文章成功");
    }

}
