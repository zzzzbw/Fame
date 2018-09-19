DROP DATABASE IF EXISTS fame;
CREATE DATABASE fame CHARACTER SET utf8 COLLATE utf8_general_ci;;
USE fame;

CREATE TABLE users (
  id           INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username     VARCHAR(45)     NOT NULL UNIQUE,
  password_md5 VARCHAR(45)     NOT NULL,
  email        VARCHAR(45),
  screen_name  VARCHAR(45),
  created      TIMESTAMP       NOT NULL DEFAULT current_timestamp,
  logged       TIMESTAMP       NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE articles (
  id            INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  title         VARCHAR(255)    NOT NULL,
  created       TIMESTAMP       NOT NULL DEFAULT current_timestamp,
  modified      TIMESTAMP       NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp,
  content       TEXT,
  author_id     INT,
  hits          INT,
  tags          VARCHAR(255),
  category      VARCHAR(255),
  status        VARCHAR(32),
  type          VARCHAR(32),
  allow_comment BOOLEAN
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE comments (
  id         INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
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
  created    TIMESTAMP       NOT NULL DEFAULT current_timestamp
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE metas (
  id   INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(255)    NOT NULL,
  type VARCHAR(45)     NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE middles (
  id   INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  a_id INT             NOT NULL,
  m_id INT             NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE logs (
  id      INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  action  VARCHAR(255),
  data    TEXT,
  message VARCHAR(255),
  type    VARCHAR(255),
  ip      VARCHAR(255),
  user_id INT,
  created TIMESTAMP       NOT NULL DEFAULT current_timestamp
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO users (username, password_md5, email, screen_name)
VALUES ('fame', '3e6693e83d186225b85b09e71c974d2d', '', 'admin');

INSERT INTO articles (title, created, modified, content, author_id, hits, tags, category, status, type)
VALUES ('Hello world', now(), now(), '
欢迎使用[Fame](https://github.com/zzzzbw/Fame)! 这是你的第一篇博客。快点来写点什么吧

```java
public static void main(Stringp[] args){
    System.out.println("Hello world");
}
```

> 想要了解更多详细信息，可以查看[文档](https://github.com/zzzzbw/Fame/blob/master/README.md)。', 1, 0, 'First', 'New', 'publish', 'post');

INSERT INTO comments (article_id, content, name, email, website, agree, disagree, ip, agent) VALUES ('1', '## 测试评论
这是我的网址[Fame](http://zzzzbw.cn)', 'zzzzbw', '920049380@qq.com', 'http://zzzzbw.cn', '1', '0', '0.0.0.1', '');

INSERT INTO metas (name, type) VALUES ('First', 'tag');
INSERT INTO metas (name, type) VALUES ('New', 'category');

INSERT INTO middles (a_id, m_id) VALUES (1, 1);
INSERT INTO middles (a_id, m_id) VALUES (1, 2);

INSERT INTO articles (title, created, modified, content, author_id, hits, tags, category, status, type)
VALUES ('About', now(), now(), '# About me
### Hello word
这是关于我的页面

* [Github](https://github.com/)
* [知乎](https://www.zhihu.com/)

### 也可以设置别的页面
* 比如友链页面', 1, NULL, NULL, NULL, 'publish', 'page');
