## Fame 博客

![logo](https://zzzzbw.github.io/Fame/images/Fame_logo.png)

一个基于Spring Boot开发的博客后端。该项目前端：[Fame-default](https://github.com/zzzzbw/Fame-default)

演示站点：http://zzzzbw.cn

![spring boot](https://img.shields.io/badge/spring%20boot-v1.5.9-green.svg)![MIT](https://img.shields.io/github/license/mashape/apistatus.svg)![Maven](https://img.shields.io/badge/maven-v3.3.9-red.svg)

[API文档](https://zzzzbw.github.io/Fame/swagger-ui/index.html)

### 依赖

* JAVA 1.8+
* MAVEN 3.3.X
* MYSQL 5.7.X


###  特性

* 基于Spring-boot开发，配置和部署简单
* 前后端完全分离
* Restful风格Api
* 支持Markdown解析

## 快速开始

### 服务端

1. `git clone https://github.com/zzzzbw/Fame.git`或直接下载代码到本地

2. 将项目导入到IDE中，这是maven工程,确保你已经安装maven

3. 在mysql中执行src/main/resources/init.sql文件

4. 修改src/main/resource/application.properties文件中对应的username和password为数据库的账号密码

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/fame?useUnicode=true&characterEncoding=utf-8&useSSL=true
   spring.datasource.username=root
   spring.datasource.password=root
   ```

   ​

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

7. 可以使用[默认主题](https://github.com/zzzzbw/Fame-default)或者根据api文档开发自己的博客页面!

### 客户端

1. 到[Fame-default](https://github.com/zzzzbw/Fame-default)项目中，确保电脑中有node环境

2. `git https://github.com/zzzzbw/Fame-default.git`或直接下载代码到本地

3. 进入项目文件夹,执行命令

   ```
   npm install

   npm run dev
   ```

4.访问[localhost:8010](http://localhost:8010/)。若看到博客首页，代表部署成功！

## 部分截图（[默认主题](https://github.com/zzzzbw/Fame-default)）

![images](https://zzzzbw.github.io/Fame/images/index.png)

![images](https://zzzzbw.github.io/Fame/images/article.png)



![images](https://zzzzbw.github.io/Fame/images/Dashboard.png)

![images](https://zzzzbw.github.io/Fame/images/admin_article_edit.png)

![images](https://zzzzbw.github.io/Fame/images/admin_meta_list.png)

