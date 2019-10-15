DROP DATABASE IF EXISTS fame;
CREATE DATABASE fame CHARACTER SET utf8mb4 COLLATE utf8mb4_bin;
USE fame;

CREATE TABLE sys_option
(
    id           INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    created TIMESTAMP NOT NULL DEFAULT current_timestamp,
    modified TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
    option_key   VARCHAR(100)    NOT NULL UNIQUE,
    option_value VARCHAR(1023)   NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE user
(
    id           INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    created TIMESTAMP NOT NULL DEFAULT current_timestamp,
    modified TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
    username     VARCHAR(45)     NOT NULL UNIQUE,
    password_md5 VARCHAR(45)     NOT NULL,
    email        VARCHAR(45),
    screen_name  VARCHAR(45),
    logged       TIMESTAMP       NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE article
(
    id            INT PRIMARY KEY      NOT NULL AUTO_INCREMENT,
    created TIMESTAMP NOT NULL DEFAULT current_timestamp,
    modified TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
    title         VARCHAR(255)         NOT NULL,
    content       MEDIUMTEXT,
    author_id     INT,
    hits          INT     DEFAULT 0    NOT NULL,
    tags          VARCHAR(255),
    category      VARCHAR(255),
    status        VARCHAR(32),
    type          VARCHAR(32),
    allow_comment BOOLEAN DEFAULT TRUE NOT NULL,
    comment_count INT     DEFAULT 0    NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE comment
(
    id         INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    created TIMESTAMP NOT NULL DEFAULT current_timestamp,
    modified TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
    article_id INT             NOT NULL,
    p_id       INT,
    content    TEXT            NOT NULL,
    name       VARCHAR(255),
    email      VARCHAR(255),
    website    VARCHAR(255),
    agree      INT             NOT NULL DEFAULT 0,
    disagree   INT             NOT NULL DEFAULT 0,
    ip         VARCHAR(255),
    agent      VARCHAR(255),
    status     INT                      DEFAULT 0 NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE meta
(
    id   INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    created TIMESTAMP NOT NULL DEFAULT current_timestamp,
    modified TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
    name VARCHAR(255)    NOT NULL,
    type VARCHAR(45)     NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE middle
(
    id   INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    created TIMESTAMP NOT NULL DEFAULT current_timestamp,
    modified TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
    a_id INT             NOT NULL,
    m_id INT             NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE media
(
    id   INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    created TIMESTAMP NOT NULL DEFAULT current_timestamp,
    modified TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
    name VARCHAR(255) NOT NULL,
    url VARCHAR(1023) NOT NULL,
    thumb_url VARCHAR(1023),
    suffix VARCHAR(255) NOT NULL
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE log
(
    id      INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
    created TIMESTAMP NOT NULL DEFAULT current_timestamp,
    modified TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
    action  VARCHAR(255),
    data    TEXT,
    message VARCHAR(255),
    type    VARCHAR(255),
    ip      VARCHAR(255),
    user_id INT
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


INSERT INTO user (username, password_md5, email, screen_name)
VALUES ('fame', '3e6693e83d186225b85b09e71c974d2d', '', 'admin');


-- 描述: 将article表中status数据改为大写
INSERT INTO article (title, created, modified, content, author_id, hits, tags, category, status, type)
VALUES ('Hello world', now(), now(), '
欢迎使用[Fame](https://github.com/zzzzbw/Fame)! 这是你的第一篇博客。快点来写点什么吧

```java
public static void main(String[] args){
    System.out.println("Hello world");
}
```

> 想要了解更多详细信息，可以查看[文档](https://github.com/zzzzbw/Fame/blob/master/README.md)。', 1, 0, 'First', 'New', 'PUBLISH', 'post');


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
* 比如友链页面', 1, NULL, NULL, 'PUBLISH', 'page');

INSERT INTO sys_option (option_key, option_value)
VALUES ('fame_init', 'true');
