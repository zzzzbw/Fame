package com.zbw.fame.config;

import com.zbw.fame.model.domain.*;
import com.zbw.fame.model.enums.ArticleStatus;
import com.zbw.fame.repository.*;
import com.zbw.fame.service.OptionService;
import com.zbw.fame.util.FameUtil;
import com.zbw.fame.util.OptionKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * springboot初始化完成后执行的动作
 *
 * @author zbw
 * @since 2019/05/18 16:54
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class InitApplicationRunner implements ApplicationRunner {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    private final NoteRepository noteRepository;

    private final CategoryRepository categoryRepository;

    private final TagRepository tagRepository;

    private final MiddleRepository middleRepository;

    private final CommentRepository commentRepository;

    private final OptionService optionService;

    private static final String DEFAULT_TAG = "First";

    private static final String DEFAULT_CATEGORY = "New";

    /**
     * 用于初始化访问的链接
     */
    private static final String INIT_URL = "/api/post";

    @Value("${server.port}")
    private String port;

    @Transactional(rollbackFor = Throwable.class)
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Initializing Fame after springboot loading completed...");
        long startTime = System.currentTimeMillis();

        boolean isInit = optionService.get(OptionKeys.FAME_INIT, Boolean.FALSE);
        if (!isInit) {
            createDefaultIfAbsent();
        }

        initDispatcherServlet();

        log.info("Fame initialization in " + (System.currentTimeMillis() - startTime) + " ms");
    }

    /**
     * 访问一个连接以初始化DispatcherServlet
     */
    private void initDispatcherServlet() {
        // 任意访问一个url，使DispatcherServlet和数据库连接初始化
        String url = "http://" + FameUtil.getHostAddress() + ":" + port + INIT_URL;
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
        Post post = createDefaultPostIfAbsent(user);
        createDefaultCategoryIfAbsent(post);
        createDefaultTagIfAbsent(post);
        createDefaultCommentIfAbsent(post);
        createDefaultNoteIfAbsent(user);
        optionService.save(OptionKeys.FAME_INIT, Boolean.TRUE.toString());
        log.info("Create default data success");
    }

    private User createDefaultUserIfAbsent() {
        log.info("Create default user...");

        if (userRepository.count() > 0) {
            return null;
        }
        User user = new User();
        user.setUsername("fame");
        user.setPasswordMd5("3e6693e83d186225b85b09e71c974d2d");
        user.setEmail("");
        user.setScreenName("admin");
        userRepository.save(user);
        return user;
    }

    private Post createDefaultPostIfAbsent(User user) {
        log.info("Create default post...");
        long count = postRepository.count();
        if (null == user || count > 0) {
            return null;
        }
        Post post = new Post();
        post.setTitle("Hello world");
        post.setContent("欢迎使用[Fame](https://github.com/zzzzbw/Fame)! 这是你的第一篇博客。快点来写点什么吧\n" +
                "```java\n" +
                "public static void main(String[] args){\n" +
                "    System.out.println(\"Hello world\");\n" +
                "}\n" +
                "```\n" +
                "> 想要了解更多详细信息，可以查看[文档](https://github.com/zzzzbw/Fame/blob/master/README.md)。");
        post.setTags(DEFAULT_TAG);
        post.setCategory(DEFAULT_CATEGORY);
        post.setStatus(ArticleStatus.PUBLISH);
        post.setAuthorId(user.getId());

        postRepository.save(post);
        return post;
    }

    private void createDefaultCategoryIfAbsent(Post post) {
        log.info("Create default category...");
        long count = categoryRepository.count();
        if (null == post || count > 0) {
            return;
        }

        Category category = new Category();
        category.setName(DEFAULT_CATEGORY);
        category = categoryRepository.save(category);


        Middle middleCategory = new Middle(post.getId(), category.getId());
        middleRepository.save(middleCategory);
    }

    private void createDefaultTagIfAbsent(Post post) {
        log.info("Create default tag...");
        long count = tagRepository.count();
        if (null == post || count > 0) {
            return;
        }


        Tag tag = new Tag();
        tag.setName(DEFAULT_TAG);
        tag = tagRepository.save(tag);

        Middle middleTag = new Middle(post.getId(), tag.getId());
        middleRepository.save(middleTag);
    }

    private void createDefaultCommentIfAbsent(Post post) {
        log.info("Create default comment...");
        long count = commentRepository.count();
        if (null == post || count > 0) {
            return;
        }
        Comment comment = new Comment();
        comment.setArticleId(post.getId());
        comment.setContent("## 测试评论\n" +
                "这是我的网址[Fame](http://zzzzbw.cn)");
        comment.setName("zzzzbw");
        comment.setEmail("zzzzbw@gmail.com");
        comment.setWebsite("https://zzzzbw.cn");
        comment.setIp("0.0.0.1");
        commentRepository.save(comment);

        post.setCommentCount(1);
        postRepository.save(post);
    }

    private void createDefaultNoteIfAbsent(User user) {
        log.info("Create default page...");
        long count = noteRepository.count();
        if (null == user || count > 0) {
            return;
        }
        Note note = new Note();
        note.setTitle("About");
        note.setContent("# About me\n" +
                "### Hello word\n" +
                "这是关于我的页面\n" +
                "* [Github](https://github.com/)\n" +
                "* [知乎](https://www.zhihu.com/)\n" +
                "### 也可以设置别的页面\n" +
                "* 比如友链页面");
        note.setStatus(ArticleStatus.PUBLISH);
        note.setAuthorId(user.getId());

        noteRepository.save(note);
    }
}
