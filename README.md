## Fame 博客

基于Spring Boot开发的博客服务器端。

博客前端:[Fame-front](https://github.com/zzzzbw/Fame-front)

博客后台:[Fame-admin](https://github.com/zzzzbw/Fame-admin)

演示站点：http://zzzzbw.cn

![travis](https://travis-ci.org/zzzzbw/Fame.svg?branch=master) ![MIT](https://img.shields.io/github/license/zzzzbw/Fame.svg) ![spring boot](https://img.shields.io/badge/spring%20boot-v1.5.9-green.svg) ![Maven](https://img.shields.io/badge/maven-v3.3.9-red.svg)

[API文档](https://zzzzbw.github.io/Fame/swagger-ui/index.html)

### 环境

* JAVA 8
* MAVEN 3.3.X
* MYSQL 5.7.X

### 依赖

* [spring-boot](https://github.com/spring-projects/spring-boot)
* [mybatis-3](https://github.com/mybatis/mybatis-3)
* [Mapper](https://github.com/abel533/Mapper)
* [Mybatis-PageHelper](https://github.com/pagehelper/Mybatis-PageHelper)
* [pegdown](https://github.com/sirthias/pegdown)

### 特性

* 基于Spring-boot开发，配置和部署简单
* 前后端完全分离
* Restful风格Api
* 自带Markdown解析

### 快速开始

1. `git clone https://github.com/zzzzbw/Fame.git`或直接下载代码到本地

2. 将项目导入到IDE中，这是maven工程,确保你已经安装maven

3. 在mysql中执行src/main/resources/init.sql文件

4. 修改src/main/resource/application.properties文件中对应的username和password为数据库的账号密码

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/fame?useUnicode=true&characterEncoding=utf-8&useSSL=true
   spring.datasource.username=root
   spring.datasource.password=root
   ```

5. 在IDE中启动Application程序中执行main方法。

6. 访问[http://127.0.0.1:9090/api/article/1](http://127.0.0.1:9090/api/article/1)，若看到一下内容则代表运行成功。

   ```json
   {
       "code": 0,
       "msg": null,
       "data": {
           "id": 1,
           "title": "Hello world",
           "created": 1514709398000,
           "modified": 1514709398000,
           "content": "<p>Welcome to ...",
           "authorId": 1,
           "hits": 0,
           "tags": "First",
           "category": "New",
           "status": "publish",
           "type": "post",
           "allowComment": null
       },
       "success": true
   }
   ```

7. 配合[博客前台](https://github.com/zzzzbw/Fame-front)和[管理后台](https://github.com/zzzzbw/Fame-admin)使用。或者根据api文档开发自己的博客页面!

### 开源协议

[MIT](https://github.com/zzzzbw/Fame/blob/master/License)