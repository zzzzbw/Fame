package com.zbw.fame.config;

import com.zbw.fame.mapper.*;
import com.zbw.fame.model.domain.*;
import com.zbw.fame.service.OptionService;
import com.zbw.fame.util.OptionKeys;
import com.zbw.fame.util.Types;
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
public class InitApplicationRunner implements ApplicationRunner {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private MetaMapper metaMapper;

    @Autowired
    private MiddleMapper middleMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private OptionService optionService;

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
        InetAddress address;
        try {
            address = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            log.error("Get InetAddress error!", e);
            return;
        }

        // 任意访问一个url，使DispatcherServlet和数据库连接初始化
        String url = "http://" + address.getHostAddress() + ":" + port + INIT_URL;
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
        createDefaultMetaIfAbsent(article);
        createDefaultCommentIfAbsent(article);
        createDefaultPageIfAbsent(user);
        optionService.save(OptionKeys.FAME_INIT, Boolean.TRUE.toString());
        log.info("Create default data success");
    }

    private User createDefaultUserIfAbsent() {
        log.info("Create default user...");
        int count = userMapper.selectAll().size();
        if (count > 0) {
            return null;
        }
        User user = new User();
        user.setUsername("fame");
        user.setPasswordMd5("3e6693e83d186225b85b09e71c974d2d");
        user.setEmail("");
        user.setScreenName("admin");
        userMapper.insertSelective(user);
        return user;
    }

    private Article createDefaultArticleIfAbsent(User user) {
        log.info("Create default article...");
        Article record = new Article();
        record.setType(Types.POST);
        int count = articleMapper.select(record).size();
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
        article.setTags(DEFAULT_TAG);
        article.setCategory(DEFAULT_CATEGORY);
        article.setStatus(Types.PUBLISH);
        article.setType(Types.POST);
        article.setAuthorId(user.getId());

        articleMapper.insertSelective(article);
        return article;
    }

    private void createDefaultMetaIfAbsent(Article article) {
        log.info("Create default meta...");
        int count = metaMapper.selectAll().size();
        if (null == article || count > 0) {
            return;
        }
        Meta tag = new Meta(DEFAULT_TAG, Types.TAG);
        Meta category = new Meta(DEFAULT_CATEGORY, Types.CATEGORY);
        metaMapper.insertSelective(tag);
        metaMapper.insertSelective(category);

        Middle middleTag = new Middle(article.getId(), tag.getId());
        Middle middleCategory = new Middle(article.getId(), category.getId());
        middleMapper.insertSelective(middleTag);
        middleMapper.insertSelective(middleCategory);
    }

    private void createDefaultCommentIfAbsent(Article article) {
        log.info("Create default comment...");
        int count = commentMapper.selectAll().size();
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
        commentMapper.insertSelective(comment);

        article.setCommentCount(1);
        articleMapper.updateByPrimaryKeySelective(article);
    }

    private void createDefaultPageIfAbsent(User user) {
        log.info("Create default page...");
        Article record = new Article();
        record.setType(Types.PAGE);
        int count = articleMapper.select(record).size();
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
        article.setStatus(Types.PUBLISH);
        article.setType(Types.PAGE);
        article.setAuthorId(user.getId());

        articleMapper.insertSelective(article);
    }
}
