
CREATE TABLE sys_option
(
    id           INT NOT NULL IDENTITY,
    option_key   VARCHAR(100)    NOT NULL UNIQUE,
    option_value VARCHAR(1023)   NOT NULL,
    created      TIMESTAMP   DEFAULT current_timestamp    NOT NULL ,
    modified     TIMESTAMP   DEFAULT current_timestamp ON UPDATE current_timestamp   NOT NULL
);

CREATE TABLE user
(
    id           INT NOT NULL IDENTITY,
    username     VARCHAR(45)     NOT NULL UNIQUE,
    password_md5 VARCHAR(45)     NOT NULL,
    email        VARCHAR(45),
    screen_name  VARCHAR(45),
    created      TIMESTAMP   DEFAULT current_timestamp    NOT NULL ,
    logged       TIMESTAMP   DEFAULT current_timestamp ON UPDATE current_timestamp    NOT NULL
) ;

CREATE TABLE article
(
    id            INT     NOT NULL IDENTITY,
    title         VARCHAR(255)         NOT NULL,
    created       TIMESTAMP      DEFAULT current_timestamp      NOT NULL ,
    modified      TIMESTAMP      DEFAULT current_timestamp ON UPDATE current_timestamp      NOT NULL ,
    content       LONGVARCHAR,
    author_id     INT,
    hits          INT     DEFAULT 0    NOT NULL,
    tags          VARCHAR(255),
    category      VARCHAR(255),
    status        VARCHAR(32),
    type          VARCHAR(32),
    allow_comment BOOLEAN DEFAULT TRUE NOT NULL,
    comment_count INT     DEFAULT 0    NOT NULL
) ;

CREATE TABLE comment
(
    id         INT PRIMARY KEY NOT NULL IDENTITY,
    article_id INT             NOT NULL,
    p_id       INT,
    content    LONGVARCHAR            NOT NULL,
    name       VARCHAR(255),
    email      VARCHAR(255),
    website    VARCHAR(255),
    agree      INT       DEFAULT 0      NOT NULL ,
    disagree   INT       DEFAULT 0      NOT NULL ,
    ip         VARCHAR(255),
    agent      VARCHAR(255),
    status     INT                      DEFAULT 0 NOT NULL,
    created    TIMESTAMP   DEFAULT current_timestamp    NOT NULL
) ;

CREATE TABLE meta
(
    id   INT PRIMARY KEY NOT NULL IDENTITY,
    name VARCHAR(255)    NOT NULL,
    type VARCHAR(45)     NOT NULL
) ;

CREATE TABLE middle
(
    id   INT PRIMARY KEY NOT NULL IDENTITY,
    a_id INT             NOT NULL,
    m_id INT             NOT NULL
);

CREATE TABLE log
(
    id      INT PRIMARY KEY NOT NULL IDENTITY,
    action  VARCHAR(255),
    data    LONGVARCHAR,
    message VARCHAR(255),
    type    VARCHAR(255),
    ip      VARCHAR(255),
    user_id INT,
    created TIMESTAMP   DEFAULT current_timestamp    NOT NULL
);


INSERT INTO user (username, password_md5, email, screen_name)
VALUES ('fame', '3e6693e83d186225b85b09e71c974d2d', '', 'admin');

INSERT INTO article (title, created, modified, content, author_id, hits, tags, category, status, type)
VALUES ('Hello world', now(), now(), '
欢迎使用[Fame](https://github.com/zzzzbw/Fame)! 这是你的第一篇博客。快点来写点什么吧
> 想要了解更多详细信息，可以查看[文档](https://github.com/zzzzbw/Fame/blob/master/README.md)。', 1, 0, 'First', 'New', 'publish', 'post');

INSERT INTO comment (article_id, content, name, email, website, agree, disagree, ip, agent)
VALUES ('1', '## 测试评论
这是我的网址[Fame](http://zzzzbw.cn)', 'zzzzbw', 'zzzzbw@gmail.com', 'https://zzzzbw.cn', '1', '0', '0.0.0.1', '');

INSERT INTO meta (name, type)
VALUES ('First', 'tag');
INSERT INTO meta (name, type)
VALUES ('New', 'category');

INSERT INTO middle (a_id, m_id)
VALUES (1, 1);
INSERT INTO middle (a_id, m_id)
VALUES (1, 2);

INSERT INTO article (title, created, modified, content, author_id, tags, category, status, type)
VALUES ('About', now(), now(), '# About me
### Hello word
这是关于我的页面
* [Github](https://github.com/)
* [知乎](https://www.zhihu.com/)
### 也可以设置别的页面
* 比如友链页面', 1, NULL, NULL, 'publish', 'page');

INSERT INTO sys_option (option_key, option_value)
VALUES ('fame_init', 'true');
