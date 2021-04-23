package com.zbw.fame.config;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zbw.fame.model.entity.*;
import com.zbw.fame.model.enums.ArticleStatus;
import com.zbw.fame.model.enums.LogType;
import com.zbw.fame.service.*;
import com.zbw.fame.util.FameConst;
import com.zbw.fame.util.FameUtils;
import com.zbw.fame.util.OptionKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

/**
 * springboot初始化完成后执行的动作
 *
 * @author zzzzbw
 * @since 2019/05/18 16:54
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InitApplicationRunner implements ApplicationRunner {

    private final UserService userService;

    private final ArticleService articleService;

    private final CategoryService categoryService;

    private final ArticleCategoryService articleCategoryService;

    private final TagService tagService;

    private final ArticleTagService articleTagService;

    private final CommentService commentService;

    private final SysOptionService sysOptionService;

    private final SysLogService sysLogService;

    private static final String DEFAULT_TAG = "First";

    private static final String DEFAULT_CATEGORY = "New";

    /**
     * 用于初始化访问的链接
     */
    private static final String INIT_URL = "/api/article";

    @Value("${server.port}")
    private String port;

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Initializing Fame after springboot loading completed...");
        long startTime = System.currentTimeMillis();

        boolean isInit = sysOptionService.get(OptionKeys.FAME_INIT, Boolean.FALSE);
        if (!isInit) {
            createDefaultIfAbsent();
        }

        initDispatcherServlet();

        log.info("Fame initialization in {}ms", (System.currentTimeMillis() - startTime));
    }

    /**
     * 访问一个连接以初始化DispatcherServlet
     */
    private void initDispatcherServlet() {
        // 任意访问一个url，使DispatcherServlet和数据库连接初始化
        String url = "http://" + FameUtils.getHostAddress() + ":" + port + INIT_URL;
        log.info("The url for init: {}", url);

        try {
            new RestTemplate().getForObject(url, String.class);
        } catch (Exception e) {
            log.error("InitApplicationRunner error", e);
        }
    }

    private void createDefaultIfAbsent() {
        log.info("Start create default data...");
        User user = createDefaultUserIfAbsent();
        Article article = createDefaultArticleIfAbsent(user);
        createDefaultCategoryIfAbsent(article);
        createDefaultTagIfAbsent(article);
        createDefaultCommentIfAbsent(article);
        createDefaultHeaderArticleIfAbsent(user);
        createDefaultOptionIfAbsent();
        createInitLog();
        sysOptionService.save(OptionKeys.FAME_INIT, Boolean.TRUE.toString());
        log.info("Create default data success");
    }

    private User createDefaultUserIfAbsent() {
        log.info("Create default user...");

        if (userService.count() > 0) {
            return null;
        }
        User user = new User();
        user.setUsername("fame");
        user.setPasswordMd5("3e6693e83d186225b85b09e71c974d2d");
        user.setEmail("");
        user.setScreenName("admin");
        userService.save(user);
        return user;
    }

    private Article createDefaultArticleIfAbsent(User user) {
        log.info("Create default post...");
        long count = articleService.count(Wrappers.<Article>lambdaQuery().eq(Article::isHeaderShow, false));
        if (null == user || count > 0) {
            return null;
        }
        Article article = new Article();
        article.setTitle("Hello world");
        article.setContent("欢迎使用[Fame](https://github.com/zzzzbw/Fame)! 这是你的第一篇博客。快点来写点什么吧\n" +
                "```java\n" +
                "public static void main(String[] args){\n" +
                "    System.out.println(\"Hello world\");\n" +
                "}\n" +
                "```\n" +
                "> 想要了解更多详细信息，可以查看[文档](https://github.com/zzzzbw/Fame/blob/master/README.md)。");
        article.setHeaderShow(false);
        article.setListShow(true);
        article.setStatus(ArticleStatus.PUBLISH);
        article.setAuthorId(user.getId());

        articleService.save(article);
        return article;
    }

    private void createDefaultCategoryIfAbsent(Article article) {
        log.info("Create default category...");
        long count = categoryService.count();
        if (null == article || count > 0) {
            return;
        }

        Category category = new Category();
        category.setName(DEFAULT_CATEGORY);
        categoryService.save(category);

        ArticleCategory articleCategory = new ArticleCategory();
        articleCategory.setArticleId(article.getId());
        articleCategory.setCategoryId(category.getId());

        articleCategoryService.save(articleCategory);
    }

    private void createDefaultTagIfAbsent(Article article) {
        log.info("Create default tag...");
        long count = tagService.count();
        if (null == article || count > 0) {
            return;
        }


        Tag tag = new Tag();
        tag.setName(DEFAULT_TAG);
        tagService.save(tag);

        ArticleTag articleTag = new ArticleTag();
        articleTag.setArticleId(article.getId());
        articleTag.setTagId(tag.getId());

        articleTagService.save(articleTag);
    }

    private void createDefaultCommentIfAbsent(Article article) {
        log.info("Create default comment...");
        long count = commentService.count();
        if (null == article || count > 0) {
            return;
        }
        Comment comment = new Comment();
        comment.setArticleId(article.getId());
        comment.setContent("## 测试评论\n" +
                "这是我的网址[Fame](http://zzzzbw.cn)");
        comment.setName("zzzzbw");
        comment.setEmail("zzzzbw@gmail.com");
        comment.setWebsite("https://zzzzbw.cn");
        comment.setIp("0.0.0.1");
        commentService.save(comment);
    }

    private void createDefaultHeaderArticleIfAbsent(User user) {
        log.info("Create default page...");
        long count = articleService.count(Wrappers.<Article>lambdaQuery().eq(Article::isHeaderShow, true));
        if (null == user || count > 0) {
            return;
        }
        Article article = new Article();
        article.setTitle("About");
        article.setContent("# About me\n" +
                "### Hello word\n" +
                "这是关于我的页面\n" +
                "* [Github](https://github.com/)\n" +
                "* [知乎](https://www.zhihu.com/)\n" +
                "### 也可以设置别的页面\n" +
                "* 比如友链页面");
        article.setHeaderShow(true);
        article.setListShow(false);
        article.setStatus(ArticleStatus.PUBLISH);
        article.setAuthorId(user.getId());

        articleService.save(article);
    }

    private void createDefaultOptionIfAbsent() {
        log.info("Create default option...");
        if (ObjectUtils.isEmpty(sysOptionService.get(OptionKeys.EMAIL_SUBJECT))) {
            sysOptionService.save(OptionKeys.EMAIL_SUBJECT, FameConst.DEFAULT_EMAIL_TEMPLATE_SUBJECT);
        }
        if (ObjectUtils.isEmpty(sysOptionService.get(OptionKeys.SUMMARY_FLAG))) {
            sysOptionService.save(OptionKeys.SUMMARY_FLAG, FameConst.DEFAULT_SUMMARY_FLAG);
        }
    }


    private void createInitLog() {
        SysLog sysLog = new SysLog();
        sysLog.setLogType(LogType.SYSTEM);
        sysLog.setMessage("Init System");
        sysLogService.save(sysLog);
    }
}
