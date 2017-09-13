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
  pre           TEXT,
  author_id     INT,
  hits          INT,
  tags          VARCHAR(255),
  category      VARCHAR(255),
  status        VARCHAR(32),
  allow_comment BOOLEAN
);

CREATE TABLE metas (
  id         INT PRIMARY KEY NOT NULL AUTO_INCREMENT,
  name       VARCHAR(255)    NOT NULL,
  type       VARCHAR(45)     NOT NULL,
  article_id INT             NOT NULL
);
