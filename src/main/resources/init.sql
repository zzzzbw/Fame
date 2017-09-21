DROP DATABASE IF EXISTS fame;
CREATE DATABASE fame;
USE fame;

CREATE TABLE users (
  id           INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username     VARCHAR(45)     NOT NULL UNIQUE,
  password_md5 VARCHAR(45)     NOT NULL,
  email        VARCHAR(45),
  screen_name  VARCHAR(45),
  created      DATETIME,
  logged       DATETIME
);

CREATE TABLE articles (
  id            INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  title         VARCHAR(255)    NOT NULL,
  created       DATETIME,
  modified      DATETIME,
  content       TEXT,
  author_id     INT,
  hits          INT,
  tags          VARCHAR(255),
  category      VARCHAR(255),
  status        VARCHAR(32),
  allow_comment BOOLEAN
);

CREATE TABLE metas (
  id   INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name VARCHAR(255)    NOT NULL,
  type VARCHAR(45)     NOT NULL
);

CREATE TABLE middles (
  id   INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  a_id INT             NOT NULL,
  m_id INT             NOT NULL
);


INSERT INTO users (username, password_md5, email, screen_name) VALUES ('zbw', '123', '920049380@qq.com', 'zzzzbw');

INSERT INTO articles (title, created, modified, content, author_id, hits, tags, category, status)
VALUES ('Hello world', now(), now(), 'Welcome to [Hexo](https://hexo.io/)! This is your very first post. Check [documentation](https://hexo.io/docs/) for more info. If you get any problems when using Hexo, you can find the answer in [troubleshooting](https://hexo.io/docs/troubleshooting.html) or you can ask me on [GitHub](https://github.com/hexojs/hexo/issues).

 ## Quick Start

 ### Create a new post

 ``` bash
 $ hexo new "My New Post"
 ```
 <!--read more-->
 More info: [Writing](https://hexo.io/docs/writing.html)

 ### Run server

 ``` bash
 $ hexo server
 ```

 More info: [Server](https://hexo.io/docs/server.html)

 ### Generate static files

 ``` bash
 $ hexo generate
 ```

 More info: [Generating](https://hexo.io/docs/generating.html)

 ### Deploy to remote sites

 ``` bash
 $ hexo deploy
 ```

 More info: [Deployment](https://hexo.io/docs/deployment.html)', 1, 0, 'First', 'New', 'publish');

INSERT INTO metas (name, type) VALUES ('First', 'tag');
INSERT INTO metas (name, type) VALUES ('New', 'category');

INSERT INTO middles (a_id, m_id) VALUES (1, 1);
INSERT INTO middles (a_id, m_id) VALUES (1, 2);
