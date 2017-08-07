CREATE DATABASE fame;
use fame;

CREATE TABLE users(
  id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  username VARCHAR(45) NOT NULL UNIQUE ,
  password_md5 VARCHAR(45) NOT NULL ,
  email VARCHAR(45),
  screen_name VARCHAR(45),
  created DATETIME,
  logged DATETIME
);

CREATE TABLE contents(
  id int PRIMARY KEY NOT NULL AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL ,
  created DATETIME,
  modified DATETIME,
  content TEXT,
  author_id INT,
  hits INT,
  tags VARCHAR(255),
  status VARCHAR(32),
  allow_comment BOOLEAN
);
